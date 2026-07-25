package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.application.nationaldefencemanagementsystem.DTOs.VehicleCategoryDto;
import it.application.nationaldefencemanagementsystem.Services.VehicleCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle-categories")
@Tag(name = "Vehicle Categories", description = "Vehicle category management: view and manage types of military vehicles")
public class VehicleCategoryController extends AbstractController<VehicleCategoryDto> {

    private final VehicleCategoryService vehicleCategoryService;

    public VehicleCategoryController(VehicleCategoryService vehicleCategoryService) {
        this.service = vehicleCategoryService;
        this.vehicleCategoryService = vehicleCategoryService;
    }

    @GetMapping
    @Operation(summary = "Get all vehicle categories", description = "Returns the complete list of available vehicle categories." +
            "Use at least one parameter.")
    public List<VehicleCategoryDto> index() {
        return vehicleCategoryService.index();
    }
}