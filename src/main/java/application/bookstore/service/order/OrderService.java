package application.bookstore.service.order;

import application.bookstore.dto.OrderPlacementRequestDto;
import application.bookstore.dto.order.OrderDto;
import application.bookstore.dto.order.UpdateOrderStatusRequestDto;
import application.bookstore.dto.orderitem.OrderItemDto;
import java.util.List;

public interface OrderService {
    OrderDto placeOrderByUserId(Long userId, OrderPlacementRequestDto request);

    List<OrderDto> getOrdersByUserId(Long userId);

    OrderDto updateOrderStatus(Long userId, Long orderId, UpdateOrderStatusRequestDto request);

    List<OrderItemDto> getOrderItemsFromOrder(Long userId, Long orderId);

    OrderItemDto getSpecificItemFromOrder(Long userId, Long orderId, Long itemId);
}
