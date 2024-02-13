package application.bookstore.repository.book;

import application.bookstore.model.Book;
import application.bookstore.repository.SpecificationProvider;
import application.bookstore.repository.SpecificationProviderManager;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private List<SpecificationProvider<Book>> bookSpecificationProviders;

    public BookSpecificationProviderManager(List<SpecificationProvider<Book>>
                                                    bookSpecificationProviders) {
        this.bookSpecificationProviders = bookSpecificationProviders;
    }

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find correct "
                        + "specification provider for key " + key));
    }
}
