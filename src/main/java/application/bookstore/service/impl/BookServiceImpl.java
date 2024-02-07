package application.bookstore.service.impl;

import application.bookstore.dto.BookDto;
import application.bookstore.dto.CreateBookRequestDto;
import application.bookstore.mapper.BookMapper;
import application.bookstore.model.Book;
import application.bookstore.repository.BookRepository;
import application.bookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toModel(bookDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.getBookById(id);
        return bookMapper.toDto(book);
    }
}
