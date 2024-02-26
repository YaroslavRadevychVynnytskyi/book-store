package application.bookstore.mapper;

import application.bookstore.config.MapperConfig;
import application.bookstore.dto.user.UserRegistrationRequestDto;
import application.bookstore.dto.user.UserResponseDto;
import application.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toResponseDto(User user);
}
