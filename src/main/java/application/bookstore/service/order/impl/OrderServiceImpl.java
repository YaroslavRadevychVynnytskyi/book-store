package application.bookstore.service.order.impl;

import application.bookstore.dto.OrderPlacementRequestDto;
import application.bookstore.dto.order.OrderDto;
import application.bookstore.dto.order.UpdateOrderStatusRequestDto;
import application.bookstore.dto.orderitem.OrderItemDto;
import application.bookstore.exception.EntityNotFoundException;
import application.bookstore.mapper.OrderItemMapper;
import application.bookstore.mapper.OrderMapper;
import application.bookstore.model.CartItem;
import application.bookstore.model.Order;
import application.bookstore.model.OrderItem;
import application.bookstore.model.ShoppingCart;
import application.bookstore.model.User;
import application.bookstore.repository.order.OrderRepository;
import application.bookstore.repository.orderitem.OrderItemRepository;
import application.bookstore.repository.shopping.cart.CartItemRepository;
import application.bookstore.repository.shopping.cart.ShoppingCartRepository;
import application.bookstore.repository.user.UserRepository;
import application.bookstore.service.order.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderDto placeOrderByUserId(Long userId, OrderPlacementRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't get user by id: " + userId));
        Order order = buildOrder(user, request.shippingAddress());
        order = orderRepository.save(order);
        clearCartByUserId(userId);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto updateOrderStatus(Long userId,
                                      Long orderId,
                                      UpdateOrderStatusRequestDto request
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find "
                        + "order with id: " + orderId));
        order.setStatus(request.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getOrderItemsFromOrder(Long userId, Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderIdAndUserId(orderId, userId);
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getSpecificItemFromOrder(Long userId, Long orderId, Long itemId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderIdAndUserId(orderId, userId);
        OrderItem requiredItem = orderItems.stream()
                .filter(orderItem -> orderItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can't find order"
                        + " with orderId: " + orderId));
        return orderItemMapper.toDto(requiredItem);
    }

    private Order buildOrder(User user, String address) {
        Order order = Order.builder()
                .user(user)
                .status(Order.Status.PENDING)
                .total(BigDecimal.ZERO)
                .orderDate(LocalDateTime.now())
                .shippingAddress(address)
                .build();
        List<OrderItem> orderItemList = getOrderItems(user.getId());
        orderItemList.forEach(orderItem -> orderItem.setOrder(order));
        order.setTotal(calculateTotal(orderItemList));

        Set<OrderItem> orderItemSet = new TreeSet<>(getComparator());
        orderItemSet.addAll(orderItemList);
        order.setOrderItems(orderItemSet);
        return order;
    }

    private BigDecimal calculateTotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderItem> getOrderItems(Long id) {
        ShoppingCart cart = cartRepository.findShoppingCartByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find "
                        + "shopping cart by user id: " + id));
        Set<CartItem> cartItems = cart.getCartItems();
        return cartItems.stream()
                .map(orderItemMapper::cartItemToOrderItem)
                .toList();
    }

    private Comparator<OrderItem> getComparator() {
        return (item1, item2) -> {
            int quantityComparison = Integer.compare(item1.getQuantity(), item2.getQuantity());
            if (quantityComparison == 0) {
                return item1.getPrice().compareTo(item2.getPrice());
            }
            return quantityComparison;
        };
    }

    private void clearCartByUserId(Long id) {
        cartItemRepository.deleteCartItemsByShoppingCartId(id);
    }
}
