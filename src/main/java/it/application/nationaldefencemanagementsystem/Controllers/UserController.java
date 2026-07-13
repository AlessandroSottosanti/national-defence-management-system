package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.RegisterUserDto;
import it.application.nationaldefencemanagementsystem.DTOs.UserDto;
import it.application.nationaldefencemanagementsystem.Services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<UserDto> {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UserDto register(
            @RequestBody RegisterUserDto dto
    ) {
        return service.register(dto);
    }

}