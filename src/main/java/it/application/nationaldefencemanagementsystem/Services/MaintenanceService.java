package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.MaintenanceFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.MaintenanceDto;
import it.application.nationaldefencemanagementsystem.Entities.Equipment;
import it.application.nationaldefencemanagementsystem.Entities.Maintenance;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import it.application.nationaldefencemanagementsystem.Mappers.MaintenanceMapper;
import it.application.nationaldefencemanagementsystem.Repositories.EquipmentRepository;
import it.application.nationaldefencemanagementsystem.Repositories.MaintenanceRepository;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceService
        extends AbstractService<Maintenance, MaintenanceDto> {

    private final MaintenanceRepository repository;
    private final VehicleRepository vehicleRepository;
    private final EquipmentRepository equipmentRepository;
    private final MaintenanceMapper mapper;

    public MaintenanceService(
            MaintenanceRepository repository,
            MaintenanceMapper mapper,
            VehicleRepository vehicleRepository,
            EquipmentRepository equipmentRepository
    ) {
        /*
         * Repository e mapper vengono passati
         * all'AbstractService.
         */
        super(repository, mapper);

        this.repository = repository;
        this.mapper = mapper;
        this.vehicleRepository = vehicleRepository;
        this.equipmentRepository = equipmentRepository;
    }

    /*
     * La manutenzione deve riguardare un veicolo
     * oppure un equipaggiamento.
     */
    @Override
    public MaintenanceDto insert(MaintenanceDto dto) {

        validateMaintenanceDates(dto);
        validateMaintenanceTarget(dto);

        Maintenance entity = mapper.toEntity(dto);

        if (dto.getVehicleId() != null) {

            Vehicle vehicle =
                    vehicleRepository
                            .findById(dto.getVehicleId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Veicolo non trovato con id: "
                                                    + dto.getVehicleId()
                                    )
                            );

            entity.setVehicle(vehicle);
            entity.setEquipment(null);
        }

        if (dto.getEquipmentId() != null) {

            Equipment equipment =
                    equipmentRepository
                            .findById(dto.getEquipmentId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Equipaggiamento non trovato con id: "
                                                    + dto.getEquipmentId()
                                    )
                            );

            entity.setEquipment(equipment);
            entity.setVehicle(null);
        }

        Maintenance savedEntity =
                repository.save(entity);

        return mapper.toDTO(savedEntity);
    }

    /*
     * Recuperiamo la manutenzione esistente e aggiorniamo
     * i campi senza creare un nuovo record.
     */
    @Override
    public MaintenanceDto update(MaintenanceDto dto) {

        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "L'id della manutenzione è obbligatorio "
                            + "per l'aggiornamento"
            );
        }

        validateMaintenanceDates(dto);
        validateMaintenanceTarget(dto);

        Maintenance entity =
                repository.findById(dto.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Manutenzione non trovata con id: "
                                                + dto.getId()
                                )
                        );

        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setEstimatedMaintenanceDays(
                dto.getEstimatedMaintenanceDays()
        );
        entity.setCost(dto.getCost());


        if (dto.getVehicleId() != null) {

            Vehicle vehicle =
                    vehicleRepository
                            .findById(dto.getVehicleId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Veicolo non trovato con id: "
                                                    + dto.getVehicleId()
                                    )
                            );

            entity.setVehicle(vehicle);
            entity.setEquipment(null);
        }


        if (dto.getEquipmentId() != null) {

            Equipment equipment =
                    equipmentRepository
                            .findById(dto.getEquipmentId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Equipaggiamento non trovato con id: "
                                                    + dto.getEquipmentId()
                                    )
                            );

            entity.setEquipment(equipment);
            entity.setVehicle(null);
        }

        Maintenance updatedEntity =
                repository.save(entity);

        return mapper.toDTO(updatedEntity);
    }

    /*
     * Ricerca dinamica con JPA Specification.
     */
    public List<MaintenanceDto> index(
            MaintenanceFilterDto filter
    ) {

        MaintenanceFilterDto currentFilter =
                filter != null
                        ? filter
                        : new MaintenanceFilterDto();

        Specification<Maintenance> specification =
                (root, query, cb) -> {

                    List<Predicate> predicates =
                            new ArrayList<>();

                    // Filtro tramite id del veicolo.
                    if (currentFilter.getVehicleId() != null) {
                        predicates.add(
                                cb.equal(
                                        root.get("vehicle").get("id"),
                                        currentFilter.getVehicleId()
                                )
                        );
                    }

                    // Filtro tramite id dell'equipaggiamento.
                    if (currentFilter.getEquipmentId() != null) {
                        predicates.add(
                                cb.equal(
                                        root.get("equipment").get("id"),
                                        currentFilter.getEquipmentId()
                                )
                        );
                    }


                    if (currentFilter.getDescription() != null
                            && !currentFilter
                            .getDescription()
                            .isBlank()) {

                        predicates.add(
                                cb.like(
                                        cb.lower(
                                                root.<String>get(
                                                        "description"
                                                )
                                        ),
                                        "%"
                                                + currentFilter
                                                .getDescription()
                                                .toLowerCase()
                                                + "%"
                                )
                        );
                    }

                    // Filtro esatto per data di inizio.
                    if (currentFilter.getStartDate() != null) {
                        predicates.add(
                                cb.equal(
                                        root.get("startDate"),
                                        currentFilter.getStartDate()
                                )
                        );
                    }

                    // Filtro esatto per data di fine.
                    if (currentFilter.getEndDate() != null) {
                        predicates.add(
                                cb.equal(
                                        root.get("endDate"),
                                        currentFilter.getEndDate()
                                )
                        );
                    }

                    // Filtro esatto per giorni stimati.
                    if (currentFilter
                            .getEstimatedMaintenanceDays() != null) {

                        predicates.add(
                                cb.equal(
                                        root.get(
                                                "estimatedMaintenanceDays"
                                        ),
                                        currentFilter
                                                .getEstimatedMaintenanceDays()
                                )
                        );
                    }

                    // Filtro esatto per costo.
                    if (currentFilter.getCost() != null) {
                        predicates.add(
                                cb.equal(
                                        root.get("cost"),
                                        currentFilter.getCost()
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


    private void validateMaintenanceTarget(
            MaintenanceDto dto
    ) {

        boolean hasVehicle =
                dto.getVehicleId() != null;

        boolean hasEquipment =
                dto.getEquipmentId() != null;

        if (!hasVehicle && !hasEquipment) {
            throw new IllegalArgumentException(
                    "La manutenzione deve essere associata "
                            + "a un veicolo o a un equipaggiamento"
            );
        }

        if (hasVehicle && hasEquipment) {
            throw new IllegalArgumentException(
                    "La manutenzione non può essere associata "
                            + "contemporaneamente a un veicolo "
                            + "e a un equipaggiamento"
            );
        }
    }


    private void validateMaintenanceDates(
            MaintenanceDto dto
    ) {

        if (dto.getStartDate() != null
                && dto.getEndDate() != null
                && dto.getEndDate()
                .isBefore(dto.getStartDate())) {

            throw new IllegalArgumentException(
                    "La data di fine non può essere "
                            + "precedente alla data di inizio"
            );
        }
    }
}