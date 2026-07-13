package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.EquipmentDto;
import it.application.nationaldefencemanagementsystem.Entities.Equipment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper extends AbstractConverter<Equipment,EquipmentDto>{

    final private ModelMapper mapper = new ModelMapper();

    @Override
    public Equipment toEntity(EquipmentDto dto) {return mapper.map(dto,Equipment.class);}

    @Override
    public EquipmentDto toDTO(Equipment entity) {return mapper.map(entity,EquipmentDto.class);}
}
