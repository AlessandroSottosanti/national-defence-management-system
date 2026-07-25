package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import it.application.nationaldefencemanagementsystem.Services.ServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

public abstract class AbstractController<DTO> {

    @Autowired
    protected ServiceDto<DTO> service;

    @Operation(
            summary = "Delete entity by id",
            description = "Deletes the entity with the specified id"
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @Operation(
            summary = "Update entity",
            description = "Updates the entity with the specified id"
    )
    @PutMapping("/{id}")
    public DTO update(
            @PathVariable Integer id,
            @RequestBody DTO dto
    ) {
        return service.update(dto);
    }

    @Operation(
            summary = "Create entity",
            description = "Creates a new entity"
    )
    @PostMapping
    public DTO insert(@RequestBody DTO dto) {
        return service.insert(dto);
    }

    @Operation(
            summary = "Get entity by id",
            description = "Returns the entity with the specified id"
    )
    @GetMapping("/{id}")
    public DTO read(@PathVariable Integer id) {
        return service.read(id);
    }
}
