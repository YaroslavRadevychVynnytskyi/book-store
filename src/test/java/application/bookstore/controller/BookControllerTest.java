package application.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.bookstore.dto.book.BookDto;
import application.bookstore.dto.book.CreateBookRequestDto;
import application.bookstore.model.Category;
import application.bookstore.repository.category.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = {
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql",
            "classpath:database/categories/remove-mock-categories-from-categories-table.sql",
            "classpath:database/books/remove-mock-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception {
        //Given
        Category category = new Category();
        category.setName("Test Category 1");
        category.setDescription("Test Cat. Description 1");
        Category savedCategory = categoryRepository.save(category);

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Test Book 1");
        requestDto.setAuthor("Test Author 1");
        requestDto.setIsbn("hgioe-34534-ge-435-3g3");
        requestDto.setPrice(BigDecimal.valueOf(200L));
        requestDto.setDescription("A description summarizes a book's content to give "
                + "readers a glimpse into what the book is about.");
        requestDto.setCoverImage("Test Cover Image 1");
        requestDto.setCategories(Set.of(savedCategory));

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setIsbn(requestDto.getIsbn());
        expected.setPrice(requestDto.getPrice());
        expected.setDescription(requestDto.getDescription());
        expected.setCoverImage(requestDto.getCoverImage());
        expected.setCategoriesIds(requestDto.getCategories()
                .stream()
                .map(Category::getId)
                .toList());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When
        MvcResult result = mockMvc.perform(
                post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/books/add-mock-books-to-books-table.sql",
            "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            "classpath:database/books_categories/"
                    + "add-mock-book_categories-to-book_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql",
            "classpath:database/books/remove-mock-books-from-books-table.sql",
            "classpath:database/categories/remove-mock-categories-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get all books")
    void getAll_GivenBooksInCatalog_ShouldReturnAllProducts() throws Exception {
        //Given
        List<BookDto> expected = getBookDtoListMock();

        //When
        MvcResult result = mockMvc.perform(
                get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(5, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/books/add-perfume-book-to-books-table.sql",
            "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            "classpath:database/books_categories/"
                    + "add-perfume-related-book_categories-to-book_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql",
            "classpath:database/books/remove-mock-books-from-books-table.sql",
            "classpath:database/categories/remove-mock-categories-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get book by id")
    void getBookById_GivenBook_ShouldReturnCorrectBook() throws Exception {
        //Given
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Perfume");
        expected.setAuthor("Patrick Suskind");
        expected.setIsbn("34-5346-5x-4432");
        expected.setPrice(BigDecimal.valueOf(760L));
        expected.setDescription("Perfume description");
        expected.setCoverImage("image33");
        expected.setCategoriesIds(List.of(2L));

        //When
        MvcResult result = mockMvc.perform(
                get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/books/add-mock-books-to-books-table.sql",
            "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            "classpath:database/books_categories/"
                    + "add-mock-book_categories-to-book_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

    @Sql(scripts = {
            "classpath:database/books_categories/"
                    + "remove-mock-book_categories-from-book_categories-table.sql",
            "classpath:database/books/remove-mock-books-from-books-table.sql",
            "classpath:database/categories/remove-mock-categories-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete by id")
    void deleteById_GivenBookList() throws Exception {
        //Given
        Long id = 2L;
        List<BookDto> expected = getBookDtoListMock();
        expected.remove(1);

        //When
        mockMvc.perform(delete("/api/books/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        MvcResult result = mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(4, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    private List<BookDto> getBookDtoListMock() {
        BookDto bookDto1 = new BookDto();
        bookDto1.setId(1L);
        bookDto1.setTitle("Book 1");
        bookDto1.setAuthor("Author 1");
        bookDto1.setIsbn("34-5346-4h-4564");
        bookDto1.setPrice(BigDecimal.valueOf(500L));
        bookDto1.setDescription("Description 1");
        bookDto1.setCoverImage("image1");
        bookDto1.setCategoriesIds(List.of(1L));

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);
        bookDto2.setTitle("Book 2");
        bookDto2.setAuthor("Author 2");
        bookDto2.setIsbn("34-5346-4h-4565");
        bookDto2.setPrice(BigDecimal.valueOf(520L));
        bookDto2.setDescription("Description 2");
        bookDto2.setCoverImage("image2");
        bookDto2.setCategoriesIds(List.of(1L));

        BookDto bookDto3 = new BookDto();
        bookDto3.setId(3L);
        bookDto3.setTitle("Book 3");
        bookDto3.setAuthor("Author 3");
        bookDto3.setIsbn("34-5346-4h-4566");
        bookDto3.setPrice(BigDecimal.valueOf(515L));
        bookDto3.setDescription("Description 3");
        bookDto3.setCoverImage("image3");
        bookDto3.setCategoriesIds(List.of(1L));

        BookDto bookDto4 = new BookDto();
        bookDto4.setId(4L);
        bookDto4.setTitle("Book 4");
        bookDto4.setAuthor("Author 4");
        bookDto4.setIsbn("34-5346-4h-4567");
        bookDto4.setPrice(BigDecimal.valueOf(532L));
        bookDto4.setDescription("Description 4");
        bookDto4.setCoverImage("image4");
        bookDto4.setCategoriesIds(List.of(2L));

        BookDto bookDto5 = new BookDto();
        bookDto5.setId(5L);
        bookDto5.setTitle("Book 5");
        bookDto5.setAuthor("Author 5");
        bookDto5.setIsbn("34-5346-4h-4568");
        bookDto5.setPrice(BigDecimal.valueOf(556L));
        bookDto5.setDescription("Description 5");
        bookDto5.setCoverImage("image5");
        bookDto5.setCategoriesIds(List.of(3L));

        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(bookDto1);
        bookDtoList.add(bookDto2);
        bookDtoList.add(bookDto3);
        bookDtoList.add(bookDto4);
        bookDtoList.add(bookDto5);

        return bookDtoList;
    }
}
