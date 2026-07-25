package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.application.nationaldefencemanagementsystem.DTOs.OperatorDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.OperatorFilterDto;
import it.application.nationaldefencemanagementsystem.Services.OperatorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operators")
@Tag(name = "Operators", description = "Operator management: manage personnel, assignments, and military operators")
public class OperatorController extends AbstractController<OperatorDto> {

    private final OperatorService service;

    public OperatorController(OperatorService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Filtered operator search", description = "Returns the list of operators. Uses query parameters to filter by specific criteria." +
            "These are defined in OperatorFilterDto (e.g., first name, last name, rank, or assigned base).Use at least one parameter.")
    public List<OperatorDto> index(
            @ModelAttribute OperatorFilterDto filter
    ) {
        return service.index(filter);
    }
}