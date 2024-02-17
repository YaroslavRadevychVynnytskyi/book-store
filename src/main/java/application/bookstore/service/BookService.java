package application.bookstore.service;

import application.bookstore.dto.BookDto;
import application.bookstore.dto.BookSearchParameters;
import application.bookstore.dto.CreateBookRequestDto;
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
