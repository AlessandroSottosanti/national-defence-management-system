package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.VehicleCategoryDto;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import org.springframework.stereotype.Component;

@Component
public class VehicleCategoryMapper
        extends AbstractConverter<VehicleCategory, VehicleCategoryDto> {

    @Override
    public VehicleCategory toEntity(VehicleCategoryDto dto) {

        VehicleCategory entity = new VehicleCategory();

        entity.setId(dto.getId());
        entity.setName(dto.getName());

        return entity;
    }

    @Override
    public VehicleCategoryDto toDTO(VehicleCategory entity) {

        VehicleCategoryDto dto = new VehicleCategoryDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }
}