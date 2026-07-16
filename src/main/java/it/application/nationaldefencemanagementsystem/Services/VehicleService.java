package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.VehicleFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.VehicleDto;
import it.application.nationaldefencemanagementsystem.Entities.Base;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import it.application.nationaldefencemanagementsystem.Mappers.VehicleMapper;
import it.application.nationaldefencemanagementsystem.Repositories.BaseRepository;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleCategoryRepository;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VehicleService
        extends AbstractService<Vehicle, VehicleDto> {

    private final VehicleRepository repository;
    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final BaseRepository baseRepository;
    private final VehicleMapper mapper;

    public VehicleService(
            VehicleRepository repository,
            VehicleMapper mapper,
            VehicleCategoryRepository vehicleCategoryRepository,
            BaseRepository baseRepository
    ) {
        /*
         * Repository e mapper vengono passati anche
         * all'AbstractService.
         */
        super(repository, mapper);

        this.repository = repository;
        this.mapper = mapper;
        this.vehicleCategoryRepository = vehicleCategoryRepository;
        this.baseRepository = baseRepository;
    }

    /*
     * Override necessario perché il DTO contiene gli id
     * di categoria e base, mentre l'entity contiene gli oggetti completi.
     */
    @Override
    public VehicleDto insert(VehicleDto dto) {

        VehicleCategory category =
                vehicleCategoryRepository
                        .findById(dto.getVehicleCategoryId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Categoria mezzo non trovata con id: "
                                                + dto.getVehicleCategoryId()
                                )
                        );

        Base base =
                baseRepository
                        .findById(dto.getBaseId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Base non trovata con id: "
                                                + dto.getBaseId()
                                )
                        );

        Vehicle entity = mapper.toEntity(dto);

        /*
         * La matricola viene generata solamente
         * durante l'inserimento.
         */
        entity.setMatricola(UUID.randomUUID());

        entity.setCategory(category);
        entity.setBase(base);

        Vehicle savedEntity =
                repository.save(entity);

        return mapper.toDTO(savedEntity);
    }

    /*
     * Recuperiamo prima il mezzo esistente.
     * In questo modo manteniamo la matricola UUID originale.
     */
    @Override
    public VehicleDto update(VehicleDto dto) {

        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "L'id del veicolo è obbligatorio per l'aggiornamento"
            );
        }

        Vehicle entity =
                repository.findById(dto.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Veicolo non trovato con id: "
                                                + dto.getId()
                                )
                        );

        VehicleCategory category =
                vehicleCategoryRepository
                        .findById(dto.getVehicleCategoryId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Categoria mezzo non trovata con id: "
                                                + dto.getVehicleCategoryId()
                                )
                        );

        Base base =
                baseRepository
                        .findById(dto.getBaseId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Base non trovata con id: "
                                                + dto.getBaseId()
                                )
                        );

        entity.setModello(dto.getModello());
        entity.setStato(dto.getStato());
        entity.setCategory(category);
        entity.setBase(base);

        /*
         * Non modifichiamo la matricola.
         */

        Vehicle updatedEntity =
                repository.save(entity);

        return mapper.toDTO(updatedEntity);
    }

    /*
     * Restituisce i veicoli applicando soltanto
     * i filtri valorizzati.
     */
    public List<VehicleDto> index(VehicleFilterDto filter) {

        VehicleFilterDto currentFilter =
                filter != null
                        ? filter
                        : new VehicleFilterDto();

        Specification<Vehicle> specification =
                (root, query, cb) -> {

                    List<Predicate> predicates =
                            new ArrayList<>();

                    // Filtro esatto per matricola UUID.
                    if (currentFilter.getMatricola() != null) {
                        predicates.add(
                                cb.equal(
                                        root.get("matricola"),
                                        currentFilter.getMatricola()
                                )
                        );
                    }

                    /*
                     * Ricerca parziale sul modello,
                     * senza distinzione tra maiuscole e minuscole.
                     */
                    if (currentFilter.getModello() != null
                            && !currentFilter.getModello().isBlank()) {

                        predicates.add(
                                cb.like(
                                        cb.lower(
                                                root.<String>get("modello")
                                        ),
                                        "%"
                                                + currentFilter
                                                .getModello()
                                                .toLowerCase()
                                                + "%"
                                )
                        );
                    }

                    // Filtro esatto per stato.
                    if (currentFilter.getStato() != null) {
                        predicates.add(
                                cb.equal(
                                        root.get("stato"),
                                        currentFilter.getStato()
                                )
                        );
                    }

                    // Filtro tramite id della categoria.
                    if (currentFilter
                            .getVehicleCategoryId() != null) {

                        predicates.add(
                                cb.equal(
                                        root.get("category").get("id"),
                                        currentFilter
                                                .getVehicleCategoryId()
                                )
                        );
                    }

                    // Filtro tramite id della base.
                    if (currentFilter.getBaseId() != null) {
                        predicates.add(
                                cb.equal(
                                        root.get("base").get("id"),
                                        currentFilter.getBaseId()
                                )
                        );
                    }

                    return cb.and(
                            predicates.toArray(
                                    new Predicate[0]
                            )
                    );
                };

        return converter.toDTOList(
                repository.findAll(specification)
        );
    }
}