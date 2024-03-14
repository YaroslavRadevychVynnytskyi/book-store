package application.bookstore.dto.orderitem;

public record OrderItemDto(
        Long id,
        Long bookId,
        int quantity
) {
}
