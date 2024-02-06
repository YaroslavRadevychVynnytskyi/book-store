package application.bookstore.repository;

import application.bookstore.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Book getBookById(Long id);
}
