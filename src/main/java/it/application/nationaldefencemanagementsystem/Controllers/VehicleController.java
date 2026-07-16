package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.VehicleFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.VehicleDto;
import it.application.nationaldefencemanagementsystem.Services.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController
        extends AbstractController<VehicleDto> {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {


        this.service = vehicleService;

        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleDto> index(
            @ModelAttribute VehicleFilterDto filter
    ) {
        return vehicleService.index(filter);
    }
}