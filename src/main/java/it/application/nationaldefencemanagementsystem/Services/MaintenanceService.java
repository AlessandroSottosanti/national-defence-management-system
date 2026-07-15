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
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceMapper maintenanceMapper;
    private final EquipmentRepository equipmentRepository;

    /*
     * Restituisce le manutenzioni applicando
     * esclusivamente i filtri valorizzati.
     *
     * Se non viene passato alcun filtro,
     * restituisce tutte le manutenzioni.
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

                    /*
                     * Filtra per ID del veicolo collegato.
                     */
                    if (currentFilter.getVehicleId() != null) {

                        predicates.add(
                                cb.equal(
                                        root.get("vehicle").get("id"),
                                        currentFilter.getVehicleId()
                                )
                        );
                    }

                    /*
                     * Filtra per ID dell'equipaggiamento collegato.
                     */
                    if (currentFilter.getEquipmentId() != null) {

                        predicates.add(
                                cb.equal(
                                        root.get("equipment").get("id"),
                                        currentFilter.getEquipmentId()
                                )
                        );
                    }

                    /*
                     * Ricerca parziale nella descrizione,
                     * senza distinzione tra maiuscole e minuscole.
                     */
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

                    /*
                     * Collega tutte le condizioni tramite AND.
                     */
                    return cb.and(
                            predicates.toArray(
                                    new Predicate[0]
                            )
                    );
                };

        return maintenanceRepository
                .findAll(specification)
                .stream()
                .map(maintenanceMapper::toDto)
                .toList();
    }

    /*
     * Restituisce una manutenzione tramite ID.
     */
    public MaintenanceDto findById(Integer id) {

        Maintenance maintenance =
                getMaintenanceById(id);

        return maintenanceMapper.toDto(maintenance);
    }

    /*
     * Crea una nuova manutenzione.
     *
     * La manutenzione deve riguardare un veicolo
     * oppure un equipaggiamento, mai entrambi.
     */
    public MaintenanceDto create(MaintenanceDto dto) {

        validateMaintenanceDates(dto);
        validateMaintenanceTarget(dto);

        Maintenance maintenance =
                maintenanceMapper.toEntity(dto);

        /*
         * Associazione al veicolo.
         */
        if (dto.getVehicleId() != null) {

            Vehicle vehicle =
                    getVehicleById(dto.getVehicleId());

            maintenance.setVehicle(vehicle);
            maintenance.setEquipment(null);
        }

        /*
         * Associazione all'equipaggiamento.
         */
        if (dto.getEquipmentId() != null) {

            Equipment equipment =
                    getEquipmentById(dto.getEquipmentId());

            maintenance.setEquipment(equipment);
            maintenance.setVehicle(null);
        }

        Maintenance savedMaintenance =
                maintenanceRepository.save(maintenance);

        return maintenanceMapper.toDto(
                savedMaintenance
        );
    }

    /*
     * Aggiorna una manutenzione esistente.
     */
    public MaintenanceDto update(
            Integer id,
            MaintenanceDto dto
    ) {

        validateMaintenanceDates(dto);
        validateMaintenanceTarget(dto);

        /*
         * Recuperiamo l'entity già esistente.
         */
        Maintenance maintenance =
                getMaintenanceById(id);

        maintenance.setDescription(dto.getDescription());
        maintenance.setStartDate(dto.getStartDate());
        maintenance.setEndDate(dto.getEndDate());
        maintenance.setEstimatedMaintenanceDays(
                dto.getEstimatedMaintenanceDays()
        );
        maintenance.setCost(dto.getCost());

        /*
         * Se la manutenzione riguarda un veicolo,
         * colleghiamo il veicolo e rimuoviamo
         * l'eventuale equipaggiamento precedente.
         */
        if (dto.getVehicleId() != null) {

            Vehicle vehicle =
                    getVehicleById(dto.getVehicleId());

            maintenance.setVehicle(vehicle);
            maintenance.setEquipment(null);
        }

        /*
         * Se la manutenzione riguarda un equipaggiamento,
         * colleghiamo l'equipaggiamento e rimuoviamo
         * l'eventuale veicolo precedente.
         */
        if (dto.getEquipmentId() != null) {

            Equipment equipment =
                    getEquipmentById(dto.getEquipmentId());

            maintenance.setEquipment(equipment);
            maintenance.setVehicle(null);
        }

        Maintenance updatedMaintenance =
                maintenanceRepository.save(maintenance);

        return maintenanceMapper.toDto(
                updatedMaintenance
        );
    }

    /*
     * Elimina una manutenzione tramite ID.
     */
    public void delete(Integer id) {

        Maintenance maintenance =
                getMaintenanceById(id);

        maintenanceRepository.delete(maintenance);
    }

    /*
     * Recupera una manutenzione tramite ID.
     */
    private Maintenance getMaintenanceById(Integer id) {

        return maintenanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Manutenzione non trovata con id: "
                                        + id
                        )
                );
    }

    /*
     * Recupera un veicolo tramite ID.
     */
    private Vehicle getVehicleById(Integer id) {

        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Veicolo non trovato con id: "
                                        + id
                        )
                );
    }

    /*
     * Recupera un equipaggiamento tramite ID.
     */
    private Equipment getEquipmentById(Integer id) {

        return equipmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Equipaggiamento non trovato con id: "
                                        + id
                        )
                );
    }

    /*
     * Controlla che la manutenzione sia associata
     * esattamente a uno tra Vehicle ed Equipment.
     */
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

    /*
     * Controlla che la data di fine non sia
     * precedente alla data di inizio.
     */
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