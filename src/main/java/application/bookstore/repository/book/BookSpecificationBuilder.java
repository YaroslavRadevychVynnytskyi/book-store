package application.bookstore.repository.book;

import application.bookstore.dto.BookSearchParameters;
import application.bookstore.model.Book;
import application.bookstore.repository.SpecificationBuilder;
import application.bookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(TITLE)
                    .getSpecification(searchParameters.titles()));
        }
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR)
                    .getSpecification(searchParameters.authors()));
        }
        return specification;
    }
}
