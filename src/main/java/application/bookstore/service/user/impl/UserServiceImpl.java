package application.bookstore.service.user.impl;

import application.bookstore.dto.user.UserRegistrationRequestDto;
import application.bookstore.dto.user.UserResponseDto;
import application.bookstore.exception.RegistrationException;
import application.bookstore.mapper.UserMapper;
import application.bookstore.model.Role;
import application.bookstore.model.User;
import application.bookstore.repository.role.RoleRepository;
import application.bookstore.repository.user.UserRepository;
import application.bookstore.service.user.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register. User with email: "
                    + requestDto.getEmail() + " already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role role = roleRepository.getByName(Role.RoleName.USER);
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }
}
