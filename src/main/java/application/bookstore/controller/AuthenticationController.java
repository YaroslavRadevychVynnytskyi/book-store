package application.bookstore.controller;

import application.bookstore.dto.user.UserLoginRequestDto;
import application.bookstore.dto.user.UserLoginResponseDto;
import application.bookstore.dto.user.UserRegistrationRequestDto;
import application.bookstore.dto.user.UserResponseDto;
import application.bookstore.exception.RegistrationException;
import application.bookstore.security.AuthenticationService;
import application.bookstore.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
