package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.BaseDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.BaseFilterDto;
import it.application.nationaldefencemanagementsystem.Entities.Base;
import it.application.nationaldefencemanagementsystem.Mappers.BaseMapper;
import it.application.nationaldefencemanagementsystem.Repositories.ArmedForceRepository;
import it.application.nationaldefencemanagementsystem.Repositories.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;

@Service
public class BaseService extends AbstractService<Base, BaseDto> {

    private final BaseRepository repository;
    private final ArmedForceRepository armedForceRepository;
    private final BaseMapper mapper;

    public BaseService(
            BaseRepository repository,
            ArmedForceRepository armedForceRepository,
            BaseMapper mapper
    ) {
        super(repository, mapper);

        this.repository = repository;
        this.armedForceRepository = armedForceRepository;
        this.mapper = mapper;
    }

    @Override
    public BaseDto insert(BaseDto dto) {

        Base entity = mapper.toEntity(dto);

        entity.setArmedForce(
                armedForceRepository.findById(dto.getArmedForceId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Armed Force not found with id: "
                                                + dto.getArmedForceId()
                                ))
        );

        return mapper.toDTO(
                repository.save(entity)
        );
    }

    @Override
    public BaseDto update(BaseDto dto) {

        Base entity = mapper.toEntity(dto);

        entity.setArmedForce(
                armedForceRepository.findById(dto.getArmedForceId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Armed Force not found with id: "
                                                + dto.getArmedForceId()
                                ))
        );

        return mapper.toDTO(
                repository.save(entity)
        );
    }

    public List<BaseDto> index(BaseFilterDto filter) {

        Specification<Base> specification = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null &&
                    !filter.getName().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filter.getName().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getCity() != null &&
                    !filter.getCity().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("city")),
                                "%" + filter.getCity().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getAddress() != null &&
                    !filter.getAddress().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("address")),
                                "%" + filter.getAddress().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getArmedForceId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("armedForce").get("id"),
                                filter.getArmedForceId()
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