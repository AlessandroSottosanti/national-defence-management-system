package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.UserFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.RegisterUserDto;
import it.application.nationaldefencemanagementsystem.DTOs.UserDto;
import it.application.nationaldefencemanagementsystem.Entities.Role;
import it.application.nationaldefencemanagementsystem.Entities.User;
import it.application.nationaldefencemanagementsystem.Mappers.RegisterUserMapper;
import it.application.nationaldefencemanagementsystem.Mappers.UserMapper;
import it.application.nationaldefencemanagementsystem.Repositories.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<UserDto> index(UserFilterDto filter) {

        Specification<User> specification = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getUsername() != null &&
                    !filter.getUsername().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("username")),
                                "%" + filter.getUsername().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getEmail() != null &&
                    !filter.getEmail().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("email")),
                                "%" + filter.getEmail().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getRole() != null) {

                predicates.add(
                        cb.equal(
                                root.get("role"),
                                filter.getRole()
                        )
                );
            }

            if (filter.getEnabled() != null) {

                predicates.add(
                        cb.equal(
                                root.get("enabled"),
                                filter.getEnabled()
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        return converter.toDTOList(
                repository.findAll(specification)
        );
    }
}