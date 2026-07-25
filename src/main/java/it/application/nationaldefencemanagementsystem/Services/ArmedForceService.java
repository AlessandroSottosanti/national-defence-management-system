package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.ArmedForceDto;
import it.application.nationaldefencemanagementsystem.Entities.ArmedForce;
import it.application.nationaldefencemanagementsystem.Mappers.ArmedForceMapper;
import it.application.nationaldefencemanagementsystem.Repositories.ArmedForceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArmedForceService extends AbstractService<ArmedForce, ArmedForceDto> {

    private final ArmedForceRepository repository;

    public ArmedForceService(
            ArmedForceRepository repository,
            ArmedForceMapper mapper
    ) {
        super(repository, mapper);
        this.repository = repository;
    }

    public List<ArmedForceDto> index(String name) {

        if (name == null || name.isBlank()) {
            return converter.toDTOList(repository.findAll());
        }

        return converter.toDTOList(
                repository.findByNameContainingIgnoreCase(name)
        );
    }
}