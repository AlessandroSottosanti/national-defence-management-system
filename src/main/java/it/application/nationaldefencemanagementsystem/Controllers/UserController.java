package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.UserFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.UserDto;
import it.application.nationaldefencemanagementsystem.Services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(
        name = "Users",
        description = "User management"
)
public class UserController extends AbstractController<UserDto> {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;

        /*
         * Assegniamo UserService anche al campo
         * ereditato da AbstractController.
         */
        this.service = userService;
    }

    @GetMapping
    @Operation(
            summary = "Get users",
            description = "Returns all users, optionally filtered by username, email, role or enabled status."
    )
    public List<UserDto> index(
            @ModelAttribute UserFilterDto filter
    ) {

        return userService.index(filter);
    }

    /*
     * Sovrascriviamo il read generico per evitare
     * il problema dell'AbstractService.read().
     */
    @Override
    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by id",
            description = "Returns the user with the specified id."
    )
    public UserDto read(
            @PathVariable Integer id
    ) {

        return userService.findDtoById(id);
    }
}