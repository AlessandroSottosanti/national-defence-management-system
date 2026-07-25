package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.application.nationaldefencemanagementsystem.DTOs.AuthResponseDto;
import it.application.nationaldefencemanagementsystem.DTOs.LoginDto;
import it.application.nationaldefencemanagementsystem.DTOs.RegisterUserDto;
import it.application.nationaldefencemanagementsystem.DTOs.UserDto;
import it.application.nationaldefencemanagementsystem.Services.AuthService;
import it.application.nationaldefencemanagementsystem.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "User registration and authentication"
)
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(
            UserService userService,
            AuthService authService
    ) {

        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register user",
            description = "Registers a new user with ROLE_OPERATOR."
    )
    public ResponseEntity<UserDto> register(
            @RequestBody RegisterUserDto dto
    ) {

        UserDto createdUser =
                userService.register(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Authenticates the user and returns a JWT."
    )
    public AuthResponseDto login(
            @RequestBody LoginDto dto
    ) {

        return authService.login(dto);
    }
}