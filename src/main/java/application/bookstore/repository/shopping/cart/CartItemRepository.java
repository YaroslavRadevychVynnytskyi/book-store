package application.bookstore.repository.shopping.cart;

import application.bookstore.model.CartItem;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Modifying
    @Query("UPDATE CartItem item "
            + "SET item.quantity = :quantity "
            + "WHERE item.id = :id")
    void updateCartItemQuantityById(int quantity, Long id);

    @Modifying
    @Query("UPDATE CartItem item "
            + "SET item.deleted = true "
            + "WHERE item.shoppingCart.id = :shoppingCartId")
    void deleteCartItemsByShoppingCartId(Long shoppingCartId);

    @Query("SELECT item FROM CartItem item "
            + "LEFT JOIN FETCH item.book "
            + "WHERE item.shoppingCart.id = :shoppingCartId")
    Set<CartItem> findByShoppingCartId(Long shoppingCartId);
}
