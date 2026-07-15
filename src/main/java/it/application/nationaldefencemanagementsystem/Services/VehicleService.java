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
public class VehicleService extends AbstractService<Vehicle, VehicleDto>{

    private final VehicleRepository vehicleRepository;
    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final BaseRepository baseRepository;
    private final VehicleMapper vehicleMapper;


    public VehicleService(
            VehicleRepository vehicleRepository,
            VehicleCategoryRepository vehicleCategoryRepository,
            BaseRepository baseRepository,
            VehicleMapper vehicleMapper
    ){
        super(vehicleRepository, vehicleMapper);
        this.vehicleCategoryRepository = vehicleCategoryRepository;
        this.baseRepository = baseRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }
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

        return vehicleMapper.toDTOList(
                vehicleRepository.findAll(specification)
        );
    }

    // TODO: @Override di insert e update per gestire le entità legate a Vehicle nel passaggio da DTO ad ENTITY

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