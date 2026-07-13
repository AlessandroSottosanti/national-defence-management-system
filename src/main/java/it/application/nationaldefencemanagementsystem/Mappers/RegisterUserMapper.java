package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.RegisterUserDto;
import it.application.nationaldefencemanagementsystem.Entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserMapper {

    private final ModelMapper mapper = new ModelMapper();

    public User toEntity(RegisterUserDto dto) {
        return mapper.map(dto, User.class);
    }
}