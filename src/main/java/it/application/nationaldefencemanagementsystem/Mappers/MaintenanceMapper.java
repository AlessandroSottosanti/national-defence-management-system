package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.MaintenanceDto;
import it.application.nationaldefencemanagementsystem.Entities.Maintenance;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper
        extends AbstractConverter<Maintenance, MaintenanceDto> {

    @Override
    public Maintenance toEntity(MaintenanceDto dto) {

        Maintenance entity = new Maintenance();


        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setEstimatedMaintenanceDays(
                dto.getEstimatedMaintenanceDays()
        );
        entity.setCost(dto.getCost());

        return entity;
    }

    @Override
    public MaintenanceDto toDTO(Maintenance entity) {

        MaintenanceDto dto = new MaintenanceDto();

        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setEstimatedMaintenanceDays(
                entity.getEstimatedMaintenanceDays()
        );
        dto.setCost(entity.getCost());

        if (entity.getVehicle() != null) {
            dto.setVehicleId(
                    entity.getVehicle().getId()
            );
        }

        if (entity.getEquipment() != null) {
            dto.setEquipmentId(
                    entity.getEquipment().getId()
            );
        }

        return dto;
    }
}