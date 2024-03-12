package application.bookstore.mapper;

import application.bookstore.config.MapperConfig;
import application.bookstore.dto.order.OrderDto;
import application.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);
}
