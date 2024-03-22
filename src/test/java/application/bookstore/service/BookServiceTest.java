package application.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import application.bookstore.dto.book.BookDto;
import application.bookstore.dto.book.BookDtoWithoutCategoryIds;
import application.bookstore.dto.book.CreateBookRequestDto;
import application.bookstore.exception.EntityNotFoundException;
import application.bookstore.mapper.BookMapper;
import application.bookstore.model.Book;
import application.bookstore.model.Category;
import application.bookstore.repository.book.BookRepository;
import application.bookstore.service.book.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify that correct book dto was returned after saveBook() method call")
    public void saveBook_WithValidCreateBookRequestDto_ShouldReturnCorrectBookDto() {
        //Given
        Book book = getBookMock();
        CreateBookRequestDto createBookRequestDto = getCreateBookRequestDto(book);
        BookDto bookDto = getBookDtoMock(book);

        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        //When
        BookDto actual = bookService.save(createBookRequestDto);

        //Then
        assertEquals(bookDto, actual);

        verify(bookRepository, times(1)).save(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that updated book dto was returned "
            + "from updateBook() when received valid input data")
    public void updateBook_WithValidCreateBookRequestDtoAndId_ShouldReturnUpdatedBookDto() {
        //Given
        Book book = getBookMock();
        CreateBookRequestDto createBookRequestDto = getCreateBookRequestDto(book);
        createBookRequestDto.setDescription("Updated description");
        BookDto bookDto = getBookDtoMock(book);
        bookDto.setDescription("Updated description");
        book.setDescription("Updated description");

        Long id = 1L;

        when(bookRepository.existsById(id))
                .thenReturn(true);
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        //When
        BookDto actual = bookService.updateBook(createBookRequestDto, id);

        //Then
        assertEquals(bookDto, actual);

        verify(bookRepository, times(1)).existsById(id);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toModel(createBookRequestDto);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);

    }

    @Test
    @DisplayName("Verify that EntityNotFoundException was thrown "
            + "in updateBook() when passed non existing book id")
    public void updateBook_WithNonExistingId_ShouldThrowEntityNotFoundException() {
        //Given
        Long nonExistingId = 444L;

        Book book = getBookMock();
        CreateBookRequestDto createBookRequestDto = getCreateBookRequestDto(book);
        createBookRequestDto.setTitle("Updated title");
        createBookRequestDto.setDescription("Updated description");

        when(!bookRepository.existsById(nonExistingId)).thenReturn(false);

        //When&Then
        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(createBookRequestDto, nonExistingId));

        verify(bookRepository, times(1)).existsById(nonExistingId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify the correct book was returned from getBookById() when passed correct id")
    public void getBookById_WithValidBookId_ShouldReturnCorrectBookDto() {
        //Given
        Long bookId = 1L;
        Book book = getBookMock();
        BookDto bookDto = getBookDtoMock(book);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        //When
        BookDto actual = bookService.getBookById(bookId);

        //Then
        assertEquals(bookDto, actual);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that EntityNotFoundException was thrown "
            + "in getBookById() when passed non existing book id")
    public void getBookById_WithNonExistingBookId_ShouldThrowEntityNotFoundException() {
        //Given
        Long nonExistingId = 234L;

        when(bookRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(nonExistingId));
        //Then
        String expected = "Can't find book by id: " + nonExistingId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

        verify(bookRepository, times(1)).findById(nonExistingId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify that list with correct dtos was returned "
            + "from findBookByCategoryId() when passed valid category id")
    public void findBookByCategoryId_WithValidBookId_ShouldReturnListOfBookDtoWithoutCategoryIds() {
        //Given
        Long categoryId = 1L;
        Book book = getBookMock();

        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getCoverImage()
        );

        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book))
                .thenReturn(bookDtoWithoutCategoryIds);

        //When
        List<BookDtoWithoutCategoryIds> actual = bookService.findBooksByCategoryId(categoryId);

        //Then
        assertEquals(List.of(bookDtoWithoutCategoryIds), actual);

        verify(bookRepository, times(1)).findAllByCategoryId(categoryId);
        verify(bookMapper, times(1)).toDtoWithoutCategories(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    private Book getBookMock() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Mock category 1");
        category.setDescription("Mock category description");

        Book book = new Book();
        Long bookId = 1L;
        book.setId(bookId);
        book.setTitle("Mock Book 1");
        book.setAuthor("Mock author 1");
        book.setIsbn("123-23gg-54g-g4wbrs-es544g");
        book.setPrice(BigDecimal.valueOf(20L));
        book.setDescription("Mock description 1");
        book.setCategories(Set.of(category));
        book.setCoverImage("image.jpg");
        return book;
    }

    private BookDto getBookDtoMock(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoriesIds(book.getCategories()
                .stream()
                .map(Category::getId)
                .toList());
        return bookDto;
    }

    private CreateBookRequestDto getCreateBookRequestDto(Book book) {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle(book.getTitle());
        createBookRequestDto.setAuthor(book.getAuthor());
        createBookRequestDto.setIsbn(book.getIsbn());
        createBookRequestDto.setPrice(book.getPrice());
        createBookRequestDto.setDescription(book.getDescription());
        createBookRequestDto.setCoverImage(book.getCoverImage());
        createBookRequestDto.setCategories(book.getCategories());
        return createBookRequestDto;
    }
}
