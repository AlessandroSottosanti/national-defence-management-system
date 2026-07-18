package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.VehicleCategoryDto;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import it.application.nationaldefencemanagementsystem.Mappers.VehicleCategoryMapper;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleCategoryService
        extends AbstractService<
        VehicleCategory,
        VehicleCategoryDto
        > {

    public VehicleCategoryService(
            VehicleCategoryRepository repository,
            VehicleCategoryMapper mapper
    ) {
        super(repository, mapper);
    }

    // x restituire tutte le categorie dei mezzi.

    public List<VehicleCategoryDto> index() {

        return converter.toDTOList(
                repository.findAll()
        );
    }
}