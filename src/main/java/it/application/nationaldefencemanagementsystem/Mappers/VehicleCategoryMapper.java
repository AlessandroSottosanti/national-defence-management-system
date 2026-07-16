package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.VehicleCategoryDto;
import it.application.nationaldefencemanagementsystem.DTOs.VehicleDto;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import org.springframework.stereotype.Component;

@Component
public class VehicleCategoryMapper extends AbstractConverter<VehicleCategory, VehicleCategoryDto>{

    @Override
    public VehicleCategoryDto toDTO(VehicleCategory vehicleCategory) {
        VehicleCategoryDto dto = new VehicleCategoryDto();

        dto.setId(vehicleCategory.getId());
        dto.setName(vehicleCategory.getName());

        return dto;
    }

    @Override
    public VehicleCategory toEntity(VehicleCategoryDto dto) {

        VehicleCategory vehicleCategory = new VehicleCategory();

        vehicleCategory.setName(dto.getName());

        return vehicleCategory;
    }

    public void updateEntity(
            VehicleCategoryDto dto,
            VehicleCategory vehicleCategory
    ) {
        vehicleCategory.setName(dto.getName());
    }
}