package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.DocumentsDto;
import it.application.nationaldefencemanagementsystem.DTOs.EquipmentDto;
import it.application.nationaldefencemanagementsystem.Entities.Documents;
import it.application.nationaldefencemanagementsystem.Entities.Equipment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper extends AbstractConverter<Documents, DocumentsDto>{

    final private ModelMapper mapper = new ModelMapper();

    @Override
    public Documents toEntity(DocumentsDto dto) {return mapper.map(dto,Documents.class);}

    @Override
    public DocumentsDto toDTO(Documents entity) {return mapper.map(entity,DocumentsDto.class);}
}
