package application.bookstore.dto.order;

import application.bookstore.model.Order;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(@NotNull Order.Status status) {
}
