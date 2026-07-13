package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.RegisterUserDto;
import it.application.nationaldefencemanagementsystem.DTOs.UserDto;
import it.application.nationaldefencemanagementsystem.Entities.Role;
import it.application.nationaldefencemanagementsystem.Entities.User;
import it.application.nationaldefencemanagementsystem.Mappers.RegisterUserMapper;
import it.application.nationaldefencemanagementsystem.Mappers.UserMapper;
import it.application.nationaldefencemanagementsystem.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<User, UserDto> {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final RegisterUserMapper registerMapper;

    public UserService(
            UserRepository repository,
            UserMapper mapper,
            RegisterUserMapper registerMapper
    ) {
        super(repository, mapper);

        this.repository = repository;
        this.mapper = mapper;
        this.registerMapper = registerMapper;
    }

    public UserDto register(RegisterUserDto dto) {

        User user = registerMapper.toEntity(dto);

        user.setRole(Role.ROLE_OPERATOR);

        user.setEnabled(true);

        return mapper.toDTO(
                repository.save(user)
        );
    }

    public UserDto findByUsername(String username) {
        return repository.findByUsername(username)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public UserDto findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDTO)
                .orElse(null);
    }
}