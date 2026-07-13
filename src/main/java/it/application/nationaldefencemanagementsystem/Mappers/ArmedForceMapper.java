package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.ArmedForceDto;
import it.application.nationaldefencemanagementsystem.Entities.ArmedForce;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ArmedForceMapper extends AbstractConverter<ArmedForce, ArmedForceDto>{

    final private ModelMapper mapper= new ModelMapper();

    @Override
    public ArmedForce toEntity(ArmedForceDto dto) {
        return mapper.map(dto,ArmedForce.class);
    }

    @Override
    public ArmedForceDto toDTO(ArmedForce entity) {
        return mapper.map(entity,ArmedForceDto.class);
    }
}