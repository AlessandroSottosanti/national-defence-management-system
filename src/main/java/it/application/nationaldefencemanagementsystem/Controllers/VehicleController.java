package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.VehicleFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.VehicleDto;
import it.application.nationaldefencemanagementsystem.Services.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@Tag(name = "Vehicles", description = "Vehicle management: search, track, and manage military vehicles")
public class VehicleController extends AbstractController<VehicleDto> {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.service = vehicleService;
        this.vehicleService = vehicleService;
    }

    @GetMapping
    @Operation(summary = "Filtered vehicle search", description = "Returns the list of vehicles. Uses query parameters to filter by specific criteria." +
            "These are defined in VehicleFilterDto (e.g., model, category ID, operational status, or base assignment).Use at least one parameter.")
    public List<VehicleDto> index(
            @ModelAttribute VehicleFilterDto filter
    ) {
        return vehicleService.index(filter);
    }
}