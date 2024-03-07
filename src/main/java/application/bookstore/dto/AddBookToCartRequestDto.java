package application.bookstore.dto;

public record AddBookToCartRequestDto(
        Long bookId,
        int quantity
) {
}
