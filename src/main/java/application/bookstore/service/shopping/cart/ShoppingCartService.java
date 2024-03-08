package application.bookstore.service.shopping.cart;

import application.bookstore.dto.AddBookToCartRequestDto;
import application.bookstore.dto.shopping.cart.ShoppingCartDto;
import application.bookstore.dto.shopping.cart.UpdateBookQuantityRequestDto;

public interface ShoppingCartService {
    ShoppingCartDto addBookByUserId(Long id, AddBookToCartRequestDto request);

    ShoppingCartDto getShoppingCartByUserId(Long id);

    ShoppingCartDto updateBookQuantity(Long id,
                                       Long cartItemId,
                                       UpdateBookQuantityRequestDto request);

    ShoppingCartDto removeBook(Long id, Long cartItemId);
}
