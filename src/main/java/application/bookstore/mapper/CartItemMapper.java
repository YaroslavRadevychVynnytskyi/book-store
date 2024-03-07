package application.bookstore.mapper;

import application.bookstore.config.MapperConfig;
import application.bookstore.dto.AddBookToCartRequestDto;
import application.bookstore.dto.shopping.cart.CartItemDto;
import application.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    CartItem toModel(AddBookToCartRequestDto requestDto);
}
