package application.bookstore.controller;

import application.bookstore.dto.AddBookToCartRequestDto;
import application.bookstore.dto.shopping.cart.ShoppingCartDto;
import application.bookstore.dto.shopping.cart.UpdateBookQuantityRequestDto;
import application.bookstore.model.User;
import application.bookstore.service.shopping.cart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for managing operations with shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add book to cart", description = "Adds book to cart")
    public ShoppingCartDto addBook(Authentication authentication,
                                   @RequestBody AddBookToCartRequestDto request) {
        User user = (User) authentication.getPrincipal();
        return cartService.addBookByUserId(user.getId(), request);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Retrieve shopping cart", description = "Provides user's shopping cart")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartService.getShoppingCartByUserId(user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update book quantity", description = "Changes quantity of book")
    public ShoppingCartDto updateBookQuantity(Authentication authentication,
                                              @PathVariable Long cartItemId,
                                              @RequestBody UpdateBookQuantityRequestDto request) {
        User user = (User) authentication.getPrincipal();
        return cartService.updateBookQuantity(user.getId(), cartItemId, request);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete a book", description = "Removes a book from user's shopping cart")
    public ShoppingCartDto removeBook(Authentication authentication,
                                      @PathVariable Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        return cartService.removeBook(user.getId(), cartItemId);
    }
}
