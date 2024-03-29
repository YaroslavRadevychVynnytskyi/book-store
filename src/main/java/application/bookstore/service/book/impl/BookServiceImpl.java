package application.bookstore.service.book.impl;

import application.bookstore.dto.book.BookDto;
import application.bookstore.dto.book.BookDtoWithoutCategoryIds;
import application.bookstore.dto.book.BookSearchParameters;
import application.bookstore.dto.book.CreateBookRequestDto;
import application.bookstore.exception.EntityNotFoundException;
import application.bookstore.mapper.BookMapper;
import application.bookstore.model.Book;
import application.bookstore.repository.book.BookRepository;
import application.bookstore.repository.book.BookSpecificationBuilder;
import application.bookstore.service.book.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toModel(bookDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find book by id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(CreateBookRequestDto bookDto, Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id: " + id + " does not exist");
        }
        Book book = bookMapper.toModel(bookDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id: " + id + " does not exist");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> searchBooks(BookSearchParameters searchParameters) {
        Specification<Book> bookSpecification =
                bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long id) {
        List<Book> books = bookRepository.findAllByCategoryId(id);
        return books.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
