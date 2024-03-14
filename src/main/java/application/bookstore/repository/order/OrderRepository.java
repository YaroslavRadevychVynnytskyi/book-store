package application.bookstore.repository.order;

import application.bookstore.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT order FROM Order order "
            + "LEFT JOIN FETCH order.orderItems items "
            + "WHERE order.user.id = :userId")
    List<Order> findAllByUserId(Long userId);
}
