package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.application.nationaldefencemanagementsystem.DTOs.EquipmentDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.EquipmentFilterDto;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentCondition;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentStatus;
import it.application.nationaldefencemanagementsystem.Services.EquipmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
@Tag(name = "Equipment", description = "Arsenal management: search, assignment, and monitoring of weapons/accessories")
public class EquipmentController extends AbstractController<EquipmentDto> {

    private final EquipmentService service;

    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Filtered equipment search", description = "Returns the list of equipment. Uses query parameters to filter by name, condition, status, etc. " +
            "Use at least one parameter.")
    public List<EquipmentDto> index(
            @ModelAttribute EquipmentFilterDto filter
    ) {
        return service.index(filter);
    }

}
