package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.BaseDto;
import it.application.nationaldefencemanagementsystem.Entities.Base;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BaseMapper extends AbstractConverter<Base, BaseDto>{

    final private ModelMapper mapper= new ModelMapper();

    @Override
    public Base toEntity(BaseDto dto) {
        return mapper.map(dto,Base.class);
    }

    @Override
    public BaseDto toDTO(Base entity) {
        return mapper.map(entity,BaseDto.class);
    }
}


