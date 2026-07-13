package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.OperatorDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.OperatorFilterDto;
import it.application.nationaldefencemanagementsystem.Entities.Operator;
import it.application.nationaldefencemanagementsystem.Mappers.OperatorMapper;
import it.application.nationaldefencemanagementsystem.Repositories.BaseRepository;
import it.application.nationaldefencemanagementsystem.Repositories.OperatorRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OperatorService extends AbstractService<Operator, OperatorDto> {

    private final OperatorRepository repository;
    private final BaseRepository baseRepository;
    private final OperatorMapper mapper;

    public OperatorService(
            OperatorRepository repository,
            OperatorMapper mapper,
            BaseRepository baseRepository
    ) {
        super(repository, mapper);

        this.repository = repository;
        this.mapper = mapper;
        this.baseRepository = baseRepository;
    }

    @Override
    public OperatorDto insert(OperatorDto dto) {

        Operator entity = mapper.toEntity(dto);

        entity.setBase(
                baseRepository.findById(dto.getBaseId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Base not found with id: " + dto.getBaseId()
                                ))
        );

        return mapper.toDTO(
                repository.save(entity)
        );
    }

    @Override
    public OperatorDto update(OperatorDto dto) {

        Operator entity = mapper.toEntity(dto);

        entity.setBase(
                baseRepository.findById(dto.getBaseId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Base not found with id: " + dto.getBaseId()
                                ))
        );

        return mapper.toDTO(
                repository.save(entity)
        );
    }

    public List<OperatorDto> index(OperatorFilterDto filter) {

        Specification<Operator> specification = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getServiceNumber() != null &&
                    !filter.getServiceNumber().isBlank()) {

                predicates.add(
                        cb.equal(
                                root.get("serviceNumber"),
                                filter.getServiceNumber()
                        )
                );
            }

            if (filter.getStatus() != null) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                filter.getStatus()
                        )
                );
            }

            if (filter.getBaseId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("base").get("id"),
                                filter.getBaseId()
                        )
                );
            }

            if (filter.getRank() != null &&
                    !filter.getRank().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("rank")),
                                "%" + filter.getRank().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getFirstName() != null &&
                    !filter.getFirstName().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("firstName")),
                                "%" + filter.getFirstName().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getLastName() != null &&
                    !filter.getLastName().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("lastName")),
                                "%" + filter.getLastName().toLowerCase() + "%"
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