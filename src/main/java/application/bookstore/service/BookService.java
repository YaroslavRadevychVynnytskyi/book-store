package application.bookstore.service;

import application.bookstore.dto.BookDto;
import application.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);
}
