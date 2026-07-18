package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.application.nationaldefencemanagementsystem.DTOs.ArmedForceDto;
import it.application.nationaldefencemanagementsystem.Services.ArmedForceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/armed-forces")
@Tag(name = "Armed Forces", description = "Armed forces management: view and search military branches")
public class ArmedForceController extends AbstractController<ArmedForceDto> {

    private final ArmedForceService service;

    public ArmedForceController(ArmedForceService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Filtered Armed Force search", description = "Returns the list of armed forces. Uses an optional query parameter to filter by name." +
            "Name (exact or partial match via name query parameter).")
    public List<ArmedForceDto> index(
            @RequestParam(required = false) String name
    ) {
        return service.index(name);
    }
}