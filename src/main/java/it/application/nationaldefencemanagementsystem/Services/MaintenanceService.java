package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.MaintenanceFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.MaintenanceDto;
import it.application.nationaldefencemanagementsystem.Entities.Maintenance;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import it.application.nationaldefencemanagementsystem.Mappers.MaintenanceMapper;
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

    /*
     * Restituisce le manutenzioni applicando solamente
     * i filtri valorizzati.
     *
     * Se nessun filtro viene passato, restituisce tutto.
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
                     * Filtra tramite l'ID del veicolo collegato.
                     *
                     * vehicle è il nome del campo presente
                     * nell'entity Maintenance.
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
                     * Ricerca parziale e case-insensitive
                     * all'interno della descrizione.
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
                     * Tutti i filtri vengono collegati con AND.
                     *
                     * Esempio:
                     * vehicle_id = 2 AND start_date = 2026-07-15
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
     */
    public MaintenanceDto create(MaintenanceDto dto) {

        validateMaintenanceDates(dto);

        /*
         * Recupera il veicolo indicato nel DTO.
         * Se non esiste, viene generato un errore.
         */
        Vehicle vehicle =
                getVehicleById(dto.getVehicleId());

        /*
         * Il mapper crea l'entity copiando
         * i dati semplici del DTO.
         */
        Maintenance maintenance =
                maintenanceMapper.toEntity(dto);

        /*
         * La relazione con Vehicle viene impostata
         * nel service, dopo aver recuperato il veicolo.
         */
        maintenance.setVehicle(vehicle);

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

        /*
         * Recupera l'entity esistente.
         * Non creiamo una nuova entity durante l'update.
         */
        Maintenance maintenance =
                getMaintenanceById(id);

        Vehicle vehicle =
                getVehicleById(dto.getVehicleId());

        maintenance.setVehicle(vehicle);
        maintenance.setDescription(dto.getDescription());
        maintenance.setStartDate(dto.getStartDate());
        maintenance.setEndDate(dto.getEndDate());
        maintenance.setEstimatedMaintenanceDays(
                dto.getEstimatedMaintenanceDays()
        );
        maintenance.setCost(dto.getCost());

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
     * Recupera una manutenzione oppure genera un errore.
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
     * Recupera il veicolo da associare alla manutenzione.
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
     * Verifica che la data di fine non sia
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