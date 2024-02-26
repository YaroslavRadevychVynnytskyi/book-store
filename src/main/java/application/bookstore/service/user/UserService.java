package application.bookstore.service.user;

import application.bookstore.dto.user.UserRegistrationRequestDto;
import application.bookstore.dto.user.UserResponseDto;
import application.bookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
