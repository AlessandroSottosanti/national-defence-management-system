package it.application.nationaldefencemanagementsystem.Services;
import it.application.nationaldefencemanagementsystem.Mappers.Converter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractService<ENTITY,DTO> implements ServiceDto<DTO> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected JpaRepository<ENTITY,Integer> repository;
    protected Converter<ENTITY,DTO> converter;

    protected AbstractService(JpaRepository<ENTITY, Integer> repository,
                              Converter<ENTITY, DTO> converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public DTO insert(DTO dto) {

        if (dto == null) {

            logger.warn(
                    "Tentativo di inserimento con DTO nullo"
            );

            throw new IllegalArgumentException(
                    "Il DTO non può essere nullo"
            );
        }

        try {

            ENTITY saved =
                    repository.save(
                            converter.toEntity(dto)
                    );

            return converter.toDTO(saved);

        } catch (Exception ex) {

            logger.error(
                    "Errore durante l'inserimento",
                    ex
            );

            throw new RuntimeException(
                    "Errore durante il salvataggio",
                    ex
            );
        }
    }

    @Override
    public DTO read(Integer id) {

        if (id == null) {

            logger.warn("Tentativo di ricerca con id nullo");

            throw new IllegalArgumentException(
                    "L'id non può essere nullo"
            );
        }

        ENTITY entity =
                repository.findById(id)
                        .orElseThrow(() -> {

                            String message =
                                    "Entità non trovata con id: " + id;

                            logger.warn(message);

                            return new java.util.NoSuchElementException(message);
                        });

        return converter.toDTO(entity);
    }

    @Override
    public DTO update(DTO dto) {
        return converter.toDTO(repository.save(converter.toEntity(dto)));
    }

    @Override
    public void delete(Integer id) {

        if (id == null) {

            logger.warn("Tentativo di eliminazione con id nullo");

            throw new IllegalArgumentException(
                    "L'id non può essere nullo"
            );
        }

        if (!repository.existsById(id)) {

            String message =
                    "Entità non trovata con id: " + id;

            logger.warn(message);

            throw new java.util.NoSuchElementException(message);
        }

        repository.deleteById(id);

        logger.info(
                "Entità eliminata con id {}",
                id
        );
    }
}