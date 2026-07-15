package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.EquipmentDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.EquipmentFilterDto;
import it.application.nationaldefencemanagementsystem.Entities.Equipment;
import it.application.nationaldefencemanagementsystem.Mappers.EquipmentMapper;
import it.application.nationaldefencemanagementsystem.Repositories.EquipmentRepository;
import it.application.nationaldefencemanagementsystem.Repositories.OperatorRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EquipmentService extends AbstractService<Equipment, EquipmentDto> {

    private final EquipmentRepository repository;
    private final OperatorRepository operatorRepository;
    private final EquipmentMapper mapper;

    public EquipmentService(
            EquipmentRepository repository,
            EquipmentMapper mapper,
            OperatorRepository operatorRepository
    ) {
        super(repository, mapper);

        this.repository = repository;
        this.mapper = mapper;
        this.operatorRepository = operatorRepository;
    }

    @Override
    public EquipmentDto insert(EquipmentDto dto) {

        Equipment entity = mapper.toEntity(dto);

        if (dto.getOperatorId() != null && dto.getOperatorId() > 0) {

            entity.setOperator(
                    operatorRepository.findById(dto.getOperatorId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Operator not found with id: " + dto.getOperatorId()
                                    ))
            );
        }

        return mapper.toDTO(
                repository.save(entity)
        );
    }

    @Override
    public EquipmentDto update(EquipmentDto dto) {

        Equipment entity = mapper.toEntity(dto);

        if (dto.getOperatorId() != null) {

            entity.setOperator(
                    operatorRepository.findById(dto.getOperatorId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Operator not found with id: " + dto.getOperatorId()
                                    ))
            );
        } else {

            entity.setOperator(null);
        }

        return mapper.toDTO(
                repository.save(entity)
        );
    }

    public List<EquipmentDto> index(EquipmentFilterDto filter) {

        Specification<Equipment> specification = (root, query, cb) -> {

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

            if (filter.getModel() != null &&
                    !filter.getModel().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("model")),
                                "%" + filter.getModel().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getCondition() != null) {

                predicates.add(
                        cb.equal(
                                root.get("condition"),
                                filter.getCondition()
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

            if (filter.getFireArm() != null) {

                predicates.add(
                        cb.equal(
                                root.get("fireArm"),
                                filter.getFireArm()
                        )
                );
            }


            if (filter.getMaxAmmunitionCount() != null) {
                predicates.add(
                        cb.lessThan(
                                root.get("ammunitionCount"),
                                filter.getMaxAmmunitionCount()
                        )
                );
            }




            // Filtro per il limite minimo di munizioni ( >= minAmmunitionCount )
            if (filter.getMinAmmunitionCount() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("ammunitionCount"),
                                filter.getMinAmmunitionCount()
                        )
                );
            }

            if (filter.getMaxAmmoCapacity() != null) {
                predicates.add(
                        cb.lessThan(
                                root.get("ammoCapacity"),
                                filter.getMaxAmmoCapacity()
                        )
                );
            }

            if (filter.getMinAmmoCapacity() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("ammoCapacity"),
                                filter.getMinAmmoCapacity()
                        )
                );
            }


            if (filter.getOperatorId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("operator").get("id"),
                                filter.getOperatorId()
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