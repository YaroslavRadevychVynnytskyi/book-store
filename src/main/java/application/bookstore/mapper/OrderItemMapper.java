package application.bookstore.mapper;

import application.bookstore.config.MapperConfig;
import application.bookstore.dto.orderitem.OrderItemDto;
import application.bookstore.model.CartItem;
import application.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "price", source = "book.price")
    OrderItem cartItemtoOrderItem(CartItem cartItem);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);
}
