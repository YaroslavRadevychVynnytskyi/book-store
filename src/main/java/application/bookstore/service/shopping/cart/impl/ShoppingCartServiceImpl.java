package application.bookstore.service.shopping.cart.impl;

import application.bookstore.dto.AddBookToCartRequestDto;
import application.bookstore.dto.shopping.cart.ShoppingCartDto;
import application.bookstore.dto.shopping.cart.UpdateBookQuantityRequestDto;
import application.bookstore.exception.EntityNotFoundException;
import application.bookstore.mapper.CartItemMapper;
import application.bookstore.mapper.ShoppingCartMapper;
import application.bookstore.model.CartItem;
import application.bookstore.model.ShoppingCart;
import application.bookstore.repository.shopping.cart.CartItemRepository;
import application.bookstore.repository.shopping.cart.ShoppingCartRepository;
import application.bookstore.service.shopping.cart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartMapper cartMapper;
    private final CartItemMapper itemMapper;
    private final CartItemRepository itemRepository;

    @Transactional
    @Override
    public ShoppingCartDto addBookByUserId(Long id, AddBookToCartRequestDto request) {
        ShoppingCart shoppingCart = getShoppingCart(id);
        CartItem cartItem = itemMapper.toModel(request);
        cartItem.setShoppingCart(shoppingCart);
        itemRepository.save(cartItem);
        return cartMapper.toDto(getShoppingCart(id));
    }

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long id) {
        return cartMapper.toDto(getShoppingCart(id));
    }

    @Override
    @Transactional
    public ShoppingCartDto updateBookQuantity(Long id,
                                              Long cartItemId,
                                              UpdateBookQuantityRequestDto request) {
        itemRepository.updateCartItemQuantityById(request.quantity(), cartItemId);
        return cartMapper.toDto(getShoppingCart(id));
    }

    @Override
    public ShoppingCartDto removeBook(Long id, Long cartItemId) {
        itemRepository.deleteById(cartItemId);
        return cartMapper.toDto(getShoppingCart(id));
    }

    private ShoppingCart getShoppingCart(Long userId) {
        return cartRepository.findShoppingCartByUserId(userId).orElseThrow(() ->
                new EntityNotFoundException("Can't find shopping cart with userId: " + userId));
    }
}
