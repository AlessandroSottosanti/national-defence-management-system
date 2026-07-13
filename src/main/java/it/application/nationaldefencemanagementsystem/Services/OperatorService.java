package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.OperatorDto;
import it.application.nationaldefencemanagementsystem.Entities.Operator;
import it.application.nationaldefencemanagementsystem.Entities.OperatorStatus;
import it.application.nationaldefencemanagementsystem.Mappers.OperatorMapper;
import it.application.nationaldefencemanagementsystem.Repositories.BaseRepository;
import it.application.nationaldefencemanagementsystem.Repositories.OperatorRepository;
import org.springframework.stereotype.Service;

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

    public OperatorDto findByServiceNumber(String serviceNumber) {
        return repository.findByServiceNumber(serviceNumber)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public List<OperatorDto> findByStatus(OperatorStatus status) {
        return converter.toDTOList(
                repository.findByStatus(status)
        );
    }

    public List<OperatorDto> findByBase(Integer baseId) {
        return converter.toDTOList(
                repository.findByBaseId(baseId)
        );
    }

    public List<OperatorDto> searchByLastName(String lastName) {
        return converter.toDTOList(
                repository.findByLastNameContainingIgnoreCase(lastName)
        );
    }
}