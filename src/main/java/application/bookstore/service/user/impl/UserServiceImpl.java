package application.bookstore.service.user.impl;

import application.bookstore.dto.user.UserRegistrationRequestDto;
import application.bookstore.dto.user.UserResponseDto;
import application.bookstore.exception.RegistrationException;
import application.bookstore.mapper.UserMapper;
import application.bookstore.model.User;
import application.bookstore.repository.user.UserRepository;
import application.bookstore.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register. User with email: "
                    + requestDto.getEmail() + " already exists");
        }
        User savedUser = userRepository.save(userMapper.toModel(requestDto));
        return userMapper.toResponseDto(savedUser);
    }
}
