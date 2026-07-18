package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.application.nationaldefencemanagementsystem.DTOs.RegisterUserDto;
import it.application.nationaldefencemanagementsystem.DTOs.UserDto;
import it.application.nationaldefencemanagementsystem.Services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management: handle user registration, authentication, and profiles")
public class UserController extends AbstractController<UserDto> {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user in the system using the provided registration details." +
            "User management: handle user registration, authentication, and profiles.")
    public UserDto register(
            @RequestBody RegisterUserDto dto
    ) {
        return service.register(dto);
    }
}