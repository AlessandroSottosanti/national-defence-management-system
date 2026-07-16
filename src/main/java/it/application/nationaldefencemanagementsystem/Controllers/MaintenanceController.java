package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.MaintenanceFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.MaintenanceDto;
import it.application.nationaldefencemanagementsystem.Services.MaintenanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController
        extends AbstractController<MaintenanceDto> {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(
            MaintenanceService maintenanceService
    ) {


        this.service = maintenanceService;


        this.maintenanceService = maintenanceService;
    }


    @GetMapping
    public List<MaintenanceDto> index(
            @ModelAttribute MaintenanceFilterDto filter
    ) {
        return maintenanceService.index(filter);
    }
}