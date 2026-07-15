package it.application.nationaldefencemanagementsystem.Services;
import it.application.nationaldefencemanagementsystem.Mappers.Converter;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractService<ENTITY,DTO> implements ServiceDto<DTO> {


    protected JpaRepository<ENTITY,Integer> repository;
    protected Converter<ENTITY,DTO> converter;

    protected AbstractService(JpaRepository<ENTITY, Integer> repository,
                              Converter<ENTITY, DTO> converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public DTO insert(DTO dto) {
        return converter.toDTO(repository.save(converter.toEntity(dto)));
    }

    @Override
    public DTO read(Integer id) {
        return converter.toDTO(repository.findById(id).isPresent() ? repository.findById(id).get() : null);
    }

    @Override
    public DTO update(DTO dto) {
        return converter.toDTO(repository.save(converter.toEntity(dto)));
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}