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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends AbstractService<User, UserDto> {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final RegisterUserMapper registerMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            UserMapper mapper,
            RegisterUserMapper registerMapper, PasswordEncoder passwordEncoder
    ) {
        super(repository, mapper);

        this.repository = repository;
        this.mapper = mapper;
        this.registerMapper = registerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    //registrazione utente nuovo
    public UserDto register(RegisterUserDto dto) {

        String username = dto.getUsername().trim();

        String email = dto.getEmail()
                .trim()
                .toLowerCase();

        if (repository.existsByUsernameIgnoreCase(username)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username già utilizzato"
            );
        }

        if (repository.existsByEmailIgnoreCase(email)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email già utilizzata"
            );
        }

        User user = registerMapper.toEntity(dto);

        user.setUsername(username);
        user.setEmail(email);

        //La password viene salvata cifrata.

        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );


        user.setRole(Role.ROLE_OPERATOR);
        user.setEnabled(true);

        User savedUser = repository.save(user);

        return mapper.toDTO(savedUser);
    }


    public User findEntityByEmail(String email) {

        return repository
                .findByEmailIgnoreCase(
                        email.trim()
                )
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Email o password non corrette"
                        )
                );
    }

    //filtro JWT.

    public User findEntityById(Integer id) {

        return repository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Utente non trovato"
                        )
                );
    }

    public UserDto findDtoById(Integer id) {

        return mapper.toDTO(
                findEntityById(id)
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



