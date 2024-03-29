package application.bookstore.mapper;

import application.bookstore.config.MapperConfig;
import application.bookstore.dto.book.BookDto;
import application.bookstore.dto.book.BookDtoWithoutCategoryIds;
import application.bookstore.dto.book.CreateBookRequestDto;
import application.bookstore.model.Book;
import application.bookstore.model.Category;
import java.util.List;
import java.util.Optional;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        List<Long> ids = book.getCategories().stream()
                .map(Category::getId)
                .toList();
        bookDto.setCategoriesIds(ids);
    }

    @Named("bookById")
    default Book bookById(Long id) {
        return Optional.ofNullable(id)
                .map(Book::new)
                .orElse(null);
    }
}
