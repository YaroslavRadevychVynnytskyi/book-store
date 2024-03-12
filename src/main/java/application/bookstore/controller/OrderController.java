package application.bookstore.controller;

import application.bookstore.dto.OrderPlacementRequestDto;
import application.bookstore.dto.order.OrderDto;
import application.bookstore.dto.order.UpdateOrderStatusRequestDto;
import application.bookstore.dto.orderitem.OrderItemDto;
import application.bookstore.model.User;
import application.bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management",
        description = "Endpoints for managing operations with order")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Place an order", description = "Places an order")
    public OrderDto placeOrder(Authentication authentication,
                            @RequestBody OrderPlacementRequestDto request) {
        User user = (User) authentication.getPrincipal();
        return orderService.placeOrderByUserId(user.getId(), request);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get order", description = "Retrieves user's order history")
    public List<OrderDto> getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrders(user.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update status", description = "Updates status of an order")
    public OrderDto updateOrderStatus(Authentication authentication,
                                      @PathVariable Long id,
                                      @RequestBody UpdateOrderStatusRequestDto request
    ) {
        User user = (User) authentication.getPrincipal();
        return orderService.updateOrderStatus(user.getId(), id, request);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items")
    @Operation(summary = "Get all order items from an order",
            description = "Provides a list of items containing in order")
    public List<OrderItemDto> getAllOrderItemsFromOrder(Authentication authentication,
                                                   @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemsFromOrder(user.getId(), orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items/{itemId}")
    @Operation(summary = "Get specific item from an order",
            description = "Provides order item relying on it's id")
    public OrderItemDto getSpecificItemFromOrder(Authentication authentication,
                                                       @PathVariable Long orderId,
                                                       @PathVariable Long itemId) {
        User user = (User) authentication.getPrincipal();
        return orderService.getSpecificItemFromOrder(user.getId(), orderId, itemId);
    }
}
