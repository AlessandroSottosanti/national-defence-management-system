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
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final BaseRepository baseRepository;
    private final VehicleMapper vehicleMapper;

    /*
     * Restituisce i veicoli applicando solamente i filtri valorizzati.
     * Se nessun filtro viene passato, restituisce tutti i veicoli.
     */
    public List<VehicleDto> index(VehicleFilterDto filter) {

        // Evita errori nel caso in cui il filtro ricevuto sia null.
        VehicleFilterDto currentFilter =
                filter != null ? filter : new VehicleFilterDto();

        Specification<Vehicle> specification = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Ricerca esatta tramite matricola UUID.
            if (currentFilter.getMatricola() != null) {
                predicates.add(
                        cb.equal(
                                root.get("matricola"),
                                currentFilter.getMatricola()
                        )
                );
            }

            /*
             * Ricerca parziale per modello.
             * Il confronto non distingue maiuscole e minuscole.
             */
            if (currentFilter.getModello() != null
                    && !currentFilter.getModello().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.<String>get("modello")),
                                "%" + currentFilter.getModello()
                                        .toLowerCase() + "%"
                        )
                );
            }

            // Ricerca esatta tramite stato del veicolo.
            if (currentFilter.getStato() != null) {
                predicates.add(
                        cb.equal(
                                root.get("stato"),
                                currentFilter.getStato()
                        )
                );
            }

            /*
             * Ricerca tramite l'ID della categoria.
             *
             * "category" deve corrispondere al nome del campo
             * presente nell'entity Vehicle.
             */
            if (currentFilter.getVehicleCategoryId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("category").get("id"),
                                currentFilter.getVehicleCategoryId()
                        )
                );
            }

            /*
             * Ricerca tramite l'ID della base.
             *
             * "base" deve corrispondere al nome del campo
             * presente nell'entity Vehicle.
             */
            if (currentFilter.getBaseId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("base").get("id"),
                                currentFilter.getBaseId()
                        )
                );
            }

            /*
             * Tutti i filtri vengono collegati tramite AND.
             *
             * Esempio:
             * stato = OPERATIVO AND base_id = 2.
             *
             * Se la lista è vuota, vengono restituiti tutti i veicoli.
             */
            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        return vehicleRepository.findAll(specification)
                .stream()
                .map(vehicleMapper::toDto)
                .toList();
    }

    /*
     * Cerca un veicolo tramite ID e lo restituisce come DTO.
     */
    public VehicleDto findById(Integer id) {

        Vehicle vehicle = getVehicleById(id);

        return vehicleMapper.toDto(vehicle);
    }

    /*
     * Crea un nuovo veicolo.
     */
    public VehicleDto create(VehicleDto dto) {

        // Recupera la categoria indicata nel DTO.
        VehicleCategory vehicleCategory =
                getVehicleCategoryById(
                        dto.getVehicleCategoryId()
                );

        // Recupera la base indicata nel DTO.
        Base base = getBaseById(dto.getBaseId());

        /*
         * Il mapper converte solamente i dati semplici:
         * modello e stato.
         */
        Vehicle vehicle = vehicleMapper.toEntity(dto);

        // La matricola viene generata automaticamente.
        vehicle.setMatricola(UUID.randomUUID());

        // Le relazioni vengono impostate nel service.
        vehicle.setCategory(vehicleCategory);
        vehicle.setBase(base);

        Vehicle savedVehicle =
                vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(savedVehicle);
    }

    /*
     * Aggiorna un veicolo già esistente.
     */
    public VehicleDto update(
            Integer id,
            VehicleDto dto
    ) {

        // Recupera il veicolo esistente.
        Vehicle vehicle = getVehicleById(id);

        // Recupera la nuova categoria.
        VehicleCategory vehicleCategory =
                getVehicleCategoryById(
                        dto.getVehicleCategoryId()
                );

        // Recupera la nuova base.
        Base base = getBaseById(dto.getBaseId());

        // Aggiorna i dati modificabili.
        vehicle.setModello(dto.getModello());
        vehicle.setStato(dto.getStato());
        vehicle.setCategory(vehicleCategory);
        vehicle.setBase(base);

        /*
         * La matricola non viene modificata:
         * deve rimanere quella generata alla creazione.
         */

        Vehicle updatedVehicle =
                vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(updatedVehicle);
    }

    /*
     * Elimina un veicolo tramite ID.
     */
    public void delete(Integer id) {

        Vehicle vehicle = getVehicleById(id);

        vehicleRepository.delete(vehicle);
    }

    /*
     * Metodo interno utilizzato per recuperare un veicolo.
     * Se il veicolo non esiste, genera un'eccezione.
     */
    private Vehicle getVehicleById(Integer id) {

        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Veicolo non trovato con id: " + id
                        )
                );
    }

    /*
     * Metodo interno utilizzato per recuperare
     * la categoria indicata nel VehicleDto.
     */
    private VehicleCategory getVehicleCategoryById(
            Integer id
    ) {

        return vehicleCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Categoria del mezzo non trovata con id: "
                                        + id
                        )
                );
    }

    /*
     * Metodo interno utilizzato per recuperare
     * la base indicata nel VehicleDto.
     */
    private Base getBaseById(Integer id) {

        return baseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Base non trovata con id: " + id
                        )
                );
    }
}