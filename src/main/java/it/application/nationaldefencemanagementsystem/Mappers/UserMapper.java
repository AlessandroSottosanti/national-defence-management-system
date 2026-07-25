package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.UserDto;
import it.application.nationaldefencemanagementsystem.Entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractConverter<User, UserDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public User toEntity(UserDto dto) {
        return mapper.map(dto, User.class);
    }

    @Override
    public UserDto toDTO(User entity) {
        return mapper.map(entity, UserDto.class);
    }
}