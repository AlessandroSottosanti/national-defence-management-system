package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.MaintenanceFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.MaintenanceDto;
import it.application.nationaldefencemanagementsystem.Services.MaintenanceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController
        extends AbstractController<MaintenanceDto> {

    private final MaintenanceService service;

    public MaintenanceController(MaintenanceService service) {
        this.service = service;
    }


    //GET

    @GetMapping
    public List<MaintenanceDto> index(

            @RequestParam(required = false)
            Integer vehicleId,

            @RequestParam(required = false)
            String description,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(required = false)
            Integer estimatedMaintenanceDays,

            @RequestParam(required = false)
            BigDecimal cost,

            @RequestParam(required = false)
            Integer equipmentId
    ) {

        MaintenanceFilterDto filter =
                new MaintenanceFilterDto();

        filter.setVehicleId(vehicleId);
        filter.setDescription(description);
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setEstimatedMaintenanceDays(
                estimatedMaintenanceDays
        );
        filter.setCost(cost);
        filter.setEquipmentId(equipmentId);

        return service.index(filter);
    }
}