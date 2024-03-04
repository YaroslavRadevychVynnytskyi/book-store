package application.bookstore.repository.book;

import application.bookstore.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.categories c "
            + "WHERE :categoryId IN (SELECT category.id FROM b.categories category)")
    List<Book> findAllByCategoryId(Long categoryId);
}
