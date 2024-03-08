package application.bookstore.repository.shopping.cart;

import application.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Modifying
    @Query("UPDATE CartItem item "
            + "SET item.quantity = :quantity "
            + "WHERE item.id = :id")
    void updateCartItemQuantityById(int quantity, Long id);
}
