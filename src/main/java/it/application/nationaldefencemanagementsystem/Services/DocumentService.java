package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.DocumentsDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.DocumentFilterDto;
import it.application.nationaldefencemanagementsystem.Entities.Documents;
import it.application.nationaldefencemanagementsystem.Mappers.DocumentMapper;
import it.application.nationaldefencemanagementsystem.Repositories.DocumentRepository;
import it.application.nationaldefencemanagementsystem.Repositories.OperatorRepository;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService extends AbstractService<Documents, DocumentsDto> {

    private final DocumentRepository repository;
    private final DocumentMapper mapper;

    // Iniettiamo i repository per le chiavi esterne
    private final OperatorRepository operatorRepository;
    private final VehicleRepository vehicleRepository;

    public DocumentService(
            DocumentRepository repository,
            DocumentMapper mapper,
            OperatorRepository operatorRepository,
            VehicleRepository vehicleRepository
    ) {
        super(repository, mapper);

        this.repository = repository;
        this.mapper = mapper;
        this.operatorRepository = operatorRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public DocumentsDto insert(DocumentsDto dto) {

        Documents entity = mapper.toEntity(dto);

        if (dto.getOperatorId() != null) {

            entity.setOperator(
                    operatorRepository.findById(dto.getOperatorId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Operator not found with id: " + dto.getOperatorId()
                                    ))
            );
        }

        if (dto.getVehicleId() != null) {

            entity.setVehicle(
                    vehicleRepository.findById(dto.getVehicleId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Vehicle not found with id: " + dto.getVehicleId()
                                    ))
            );
        }

        return mapper.toDTO(
                repository.save(entity)
        );
    }

    @Override
    public DocumentsDto update(DocumentsDto dto) {

        Documents entity = mapper.toEntity(dto);

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

        if (dto.getVehicleId() != null) {

            entity.setVehicle(
                    vehicleRepository.findById(dto.getVehicleId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Vehicle not found with id: " + dto.getVehicleId()
                                    ))
            );
        } else {

            entity.setVehicle(null);
        }

        return mapper.toDTO(
                repository.save(entity)
        );
    }

    public List<DocumentsDto> index(DocumentFilterDto filter) {

        Specification<Documents> specification = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(
                        cb.equal(root.get("id"), filter.getId())
                );
            }

            if (filter.getTitle() != null &&
                    !filter.getTitle().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + filter.getTitle().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getFilePath() != null && !filter.getFilePath().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("filePath")),
                                "%" + filter.getFilePath().toLowerCase() + "%"
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

            if (filter.getVehicleId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("vehicle").get("id"),
                                filter.getVehicleId()
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