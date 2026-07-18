package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.application.nationaldefencemanagementsystem.DTOs.BaseDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.BaseFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.OperatorFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.OperatorDto;
import it.application.nationaldefencemanagementsystem.Services.BaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bases")
@Tag(name = "Bases", description = "Base management: search, monitor, and manage military bases")
public class BaseController extends AbstractController<BaseDto> {

    private final BaseService service;

    public BaseController(BaseService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Filtered base search", description = "Returns the list of bases. Uses query parameters to filter by specific criteria." +
            "These are defined in BaseFilterDto (e.g., name, location, capacity, or operational status).")
    public List<BaseDto> index(
            @ModelAttribute BaseFilterDto filter
    ) {
        return service.index(filter);
    }
}