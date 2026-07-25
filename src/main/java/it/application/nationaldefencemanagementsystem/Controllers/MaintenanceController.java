package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.MaintenanceFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.MaintenanceDto;
import it.application.nationaldefencemanagementsystem.Services.MaintenanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
@Tag(name = "Maintenance", description = "Maintenance management: track and schedule equipment and vehicle maintenance")
public class MaintenanceController extends AbstractController<MaintenanceDto> {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.service = maintenanceService;
        this.maintenanceService = maintenanceService;
    }

    @GetMapping
    @Operation(summary = "Filtered maintenance search", description = "Returns the list of maintenance records. Uses query parameters to filter by specific criteria." +
            "These are defined in MaintenanceFilterDto (e.g., date ranges, maintenance status, vehicle ID, or equipment ID).Use at least one parameter.")
    public List<MaintenanceDto> index(
            @ModelAttribute MaintenanceFilterDto filter
    ) {
        return maintenanceService.index(filter);
    }
}