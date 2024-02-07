package application.bookstore.mapper;

import application.bookstore.config.MapperConfig;
import application.bookstore.dto.BookDto;
import application.bookstore.dto.CreateBookRequestDto;
import application.bookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
