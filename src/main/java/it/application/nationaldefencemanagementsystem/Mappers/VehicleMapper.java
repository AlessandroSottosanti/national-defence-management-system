package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.VehicleDto;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper
        extends AbstractConverter<Vehicle, VehicleDto> {

    @Override
    public Vehicle toEntity(VehicleDto dto) {

        Vehicle entity = new Vehicle();


        entity.setModello(dto.getModello());
        entity.setStato(dto.getStato());

        return entity;
    }

    @Override
    public VehicleDto toDTO(Vehicle entity) {

        VehicleDto dto = new VehicleDto();

        dto.setId(entity.getId());
        dto.setMatricola(entity.getMatricola());
        dto.setModello(entity.getModello());
        dto.setStato(entity.getStato());


        if (entity.getCategory() != null) {
            dto.setVehicleCategoryId(
                    entity.getCategory().getId()
            );
        }


        if (entity.getBase() != null) {
            dto.setBaseId(
                    entity.getBase().getId()
            );
        }

        return dto;
    }
}