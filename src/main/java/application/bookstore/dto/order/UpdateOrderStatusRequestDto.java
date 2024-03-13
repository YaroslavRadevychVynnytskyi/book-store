package application.bookstore.dto.order;

import application.bookstore.model.Order;

public record UpdateOrderStatusRequestDto(Order.Status status) {
}
