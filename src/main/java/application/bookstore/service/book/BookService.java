package application.bookstore.service.book;

import application.bookstore.dto.book.BookDto;
import application.bookstore.dto.book.BookSearchParameters;
import application.bookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto updateBook(CreateBookRequestDto bookDto, Long id);

    void deleteById(Long id);

    List<BookDto> searchBooks(BookSearchParameters searchParameters);
}
