package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.VehicleCategoryDto;
import it.application.nationaldefencemanagementsystem.Services.VehicleCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle-categories")
@RequiredArgsConstructor
public class VehicleCategoryController {

    private final VehicleCategoryService vehicleCategoryService;

    @GetMapping
    public ResponseEntity<List<VehicleCategoryDto>> findAll() {

        List<VehicleCategoryDto> categories =
                vehicleCategoryService.findAll();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleCategoryDto> findById(
            @PathVariable Integer id
    ) {

        VehicleCategoryDto category =
                vehicleCategoryService.findById(id);

        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<VehicleCategoryDto> create(
            @Valid @RequestBody VehicleCategoryDto dto
    ) {

        VehicleCategoryDto created =
                vehicleCategoryService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleCategoryDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody VehicleCategoryDto dto
    ) {

        VehicleCategoryDto updated =
                vehicleCategoryService.update(id, dto);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id
    ) {

        vehicleCategoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}