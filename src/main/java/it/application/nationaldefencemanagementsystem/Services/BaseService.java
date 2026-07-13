package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.BaseDto;
import it.application.nationaldefencemanagementsystem.Entities.Base;
import it.application.nationaldefencemanagementsystem.Mappers.BaseMapper;
import it.application.nationaldefencemanagementsystem.Repositories.ArmedForceRepository;
import it.application.nationaldefencemanagementsystem.Repositories.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<BaseDto> findByCity(String city) {
        return converter.toDTOList(
                repository.findByCity(city)
        );
    }

    public List<BaseDto> findByArmedForce(Integer armedForceId) {
        return converter.toDTOList(
                repository.findByArmedForceId(armedForceId)
        );
    }

    public List<BaseDto> searchByName(String name) {
        return converter.toDTOList(
                repository.findByNameContainingIgnoreCase(name)
        );
    }
}