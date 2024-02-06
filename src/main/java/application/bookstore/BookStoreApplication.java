package application.bookstore;

import application.bookstore.model.Book;
import application.bookstore.service.BookService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book1 = new Book();
            book1.setTitle("Victoria");
            book1.setAuthor("Knut Hamsun");
            book1.setIsbn("978-3-16-148410-0");
            book1.setPrice(BigDecimal.valueOf(359));
            book1.setDescription("Victoria is a story about the ups and downs of being in love");
            book1.setCoverImage("image1");

            Book book2 = new Book();
            book2.setTitle("Perfume");
            book2.setAuthor("Patrick Suskind");
            book2.setIsbn("978-6-33-145410-2");
            book2.setPrice(BigDecimal.valueOf(400));
            book2.setDescription("Masterpiece of twentieth century fiction");
            book2.setCoverImage("image2");

            bookService.save(book1);
            bookService.save(book2);
            List<Book> booksFromDb = bookService.findAll();
            booksFromDb.forEach(System.out::println);
        };
    }
}
