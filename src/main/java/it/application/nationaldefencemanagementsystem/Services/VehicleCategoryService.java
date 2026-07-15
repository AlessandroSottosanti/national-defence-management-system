package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.VehicleCategoryDto;
import it.application.nationaldefencemanagementsystem.DTOs.VehicleDto;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import it.application.nationaldefencemanagementsystem.Mappers.VehicleCategoryMapper;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleCategoryService extends AbstractService<VehicleCategory, VehicleDto> {

    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final VehicleCategoryMapper vehicleCategoryMapper;

    public VehicleCategoryService(
            VehicleCategoryRepository vehicleCategoryRepository,
            VehicleCategoryMapper vehicleCategoryMapper){
        super(vehicleCategoryRepository, vehicleCategoryMapper);
        this.vehicleCategoryRepository = vehicleCategoryRepository;
        this.vehicleCategoryMapper = vehicleCategoryMapper;
    }

    public List<VehicleCategoryDto> findAll() {

        return vehicleCategoryRepository.findAll()
                .stream()
                .map(vehicleCategoryMapper::toDto)
                .toList();
    }

    public VehicleCategoryDto findById(Integer id) {

        VehicleCategory vehicleCategory =
                vehicleCategoryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Categoria del mezzo non trovata"
                                )
                        );

        return vehicleCategoryMapper.toDto(vehicleCategory);
    }

    public VehicleCategoryDto create(VehicleCategoryDto dto) {

        VehicleCategory vehicleCategory =
                vehicleCategoryMapper.toEntity(dto);

        VehicleCategory savedCategory =
                vehicleCategoryRepository.save(vehicleCategory);

        return vehicleCategoryMapper.toDto(savedCategory);
    }

    public VehicleCategoryDto update(
            Integer id,
            VehicleCategoryDto dto
    ) {

        VehicleCategory vehicleCategory =
                vehicleCategoryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Categoria del mezzo non trovata"
                                )
                        );

        vehicleCategoryMapper.updateEntity(
                dto,
                vehicleCategory
        );

        VehicleCategory updatedCategory =
                vehicleCategoryRepository.save(vehicleCategory);

        return vehicleCategoryMapper.toDto(updatedCategory);
    }

    public void delete(Integer id) {

        VehicleCategory vehicleCategory =
                vehicleCategoryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Categoria del mezzo non trovata"
                                )
                        );

        vehicleCategoryRepository.delete(vehicleCategory);
    }
}