package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.OperatorDto;
import it.application.nationaldefencemanagementsystem.Entities.Operator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OperatorMapper extends AbstractConverter<Operator, OperatorDto>{

    final private ModelMapper mapper= new ModelMapper();

    @Override
    public Operator toEntity(OperatorDto dto) {
        return mapper.map(dto,Operator.class);
    }

    @Override
    public OperatorDto toDTO(Operator entity) {
        return mapper.map(entity,OperatorDto.class);
    }
}


