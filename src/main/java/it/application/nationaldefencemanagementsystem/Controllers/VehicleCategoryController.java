package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.VehicleCategoryDto;
import it.application.nationaldefencemanagementsystem.Services.VehicleCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle-categories")
public class VehicleCategoryController
        extends AbstractController<VehicleCategoryDto> {

    private final VehicleCategoryService vehicleCategoryService;

    public VehicleCategoryController(
            VehicleCategoryService vehicleCategoryService
    ) {


        this.service = vehicleCategoryService;

        this.vehicleCategoryService =
                vehicleCategoryService;
    }


    @GetMapping
    public List<VehicleCategoryDto> index() {
        return vehicleCategoryService.index();
    }
}