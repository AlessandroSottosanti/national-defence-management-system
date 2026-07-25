package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.VehicleFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.VehicleDto;
import it.application.nationaldefencemanagementsystem.Entities.Base;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import it.application.nationaldefencemanagementsystem.Entities.VehicleStatus;
import it.application.nationaldefencemanagementsystem.Mappers.VehicleMapper;
import it.application.nationaldefencemanagementsystem.Repositories.BaseRepository;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleCategoryRepository;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    // Repository principale dei veicoli.

    @Mock
    private VehicleRepository repository;

    // Repository delle relazioni del veicolo.

    @Mock
    private VehicleCategoryRepository vehicleCategoryRepository;

    @Mock
    private BaseRepository baseRepository;

    // Mapper reale.

    private VehicleMapper mapper;

    // Service testato.

    private VehicleService service;

    // Dati comuni.

    private Vehicle vehicle;
    private VehicleDto dto;
    private VehicleCategory category;
    private Base base;
    private VehicleStatus status;

    @BeforeEach
    void setUp() {

        mapper = new VehicleMapper();

        service = new VehicleService(
                repository,
                mapper,
                vehicleCategoryRepository,
                baseRepository
        );

        /*
         * Utilizziamo il primo valore disponibile nell'enum
         * per non dipendere dal nome preciso dello stato.
         */

        status = VehicleStatus.values()[0];

        category = new VehicleCategory();
        category.setId(1);
        category.setName("Mezzi terrestri");

        base = new Base();
        base.setId(1);

        vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setMatricola(UUID.randomUUID());
        vehicle.setModello("Iveco Lince");
        vehicle.setStato(status);
        vehicle.setCategory(category);
        vehicle.setBase(base);

        dto = new VehicleDto();
        dto.setId(1);
        dto.setMatricola(vehicle.getMatricola());
        dto.setModello("Iveco Lince");
        dto.setStato(status);
        dto.setVehicleCategoryId(1);
        dto.setBaseId(1);
    }

    // =========================================================
    // INSERT
    // =========================================================

    @Test
    void insert_ShouldSaveVehicle_WhenCategoryAndBaseExist() {

        when(vehicleCategoryRepository.findById(1))
                .thenReturn(Optional.of(category));

        when(baseRepository.findById(1))
                .thenReturn(Optional.of(base));

        when(repository.save(any(Vehicle.class)))
                .thenReturn(vehicle);

        VehicleDto result =
                service.insert(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(
                "Iveco Lince",
                result.getModello()
        );
        assertEquals(
                status,
                result.getStato()
        );
        assertEquals(
                1,
                result.getVehicleCategoryId()
        );
        assertEquals(
                1,
                result.getBaseId()
        );
        assertNotNull(result.getMatricola());

        verify(vehicleCategoryRepository)
                .findById(1);

        verify(baseRepository)
                .findById(1);

        verify(repository)
                .save(any(Vehicle.class));
    }

    @Test
    void insert_ShouldThrowException_WhenCategoryNotFound() {

        when(vehicleCategoryRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.insert(dto)
        );

        // Se la categoria non esiste non si salva il veicolo.

        verify(repository, never())
                .save(any(Vehicle.class));
    }

    @Test
    void insert_ShouldThrowException_WhenBaseNotFound() {

        when(vehicleCategoryRepository.findById(1))
                .thenReturn(Optional.of(category));

        when(baseRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.insert(dto)
        );

        // Se la base non esiste non si salva il veicolo.

        verify(repository, never())
                .save(any(Vehicle.class));
    }

    // =========================================================
    // READ
    // =========================================================

    @Test
    void read_ShouldReturnVehicle_WhenIdExists() {

        when(repository.findById(1))
                .thenReturn(Optional.of(vehicle));

        VehicleDto result =
                service.read(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(
                "Iveco Lince",
                result.getModello()
        );
        assertEquals(
                1,
                result.getVehicleCategoryId()
        );
        assertEquals(
                1,
                result.getBaseId()
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void update_ShouldSaveVehicle_WhenDataExist() {

        dto.setModello(
                "Iveco Lince aggiornato"
        );

        when(repository.findById(1))
                .thenReturn(Optional.of(vehicle));

        when(vehicleCategoryRepository.findById(1))
                .thenReturn(Optional.of(category));

        when(baseRepository.findById(1))
                .thenReturn(Optional.of(base));

        when(repository.save(any(Vehicle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VehicleDto result =
                service.update(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(
                "Iveco Lince aggiornato",
                result.getModello()
        );

        verify(repository)
                .findById(1);

        verify(vehicleCategoryRepository)
                .findById(1);

        verify(baseRepository)
                .findById(1);

        verify(repository)
                .save(any(Vehicle.class));
    }

    @Test
    void update_ShouldThrowException_WhenIdIsNull() {

        dto.setId(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.update(dto)
        );

        verify(repository, never())
                .save(any(Vehicle.class));
    }

    @Test
    void update_ShouldThrowException_WhenVehicleNotFound() {

        dto.setId(99);

        when(repository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.update(dto)
        );

        verify(repository, never())
                .save(any(Vehicle.class));
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void delete_ShouldDeleteVehicle() {

        service.delete(1);

        verify(repository)
                .deleteById(1);
    }

    // =========================================================
    // INDEX
    // =========================================================

    @Test
    void index_ShouldReturnAllVehicles_WhenFilterIsEmpty() {

        VehicleFilterDto filter =
                new VehicleFilterDto();

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(vehicle));

        List<VehicleDto> result =
                service.index(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                "Iveco Lince",
                result.getFirst().getModello()
        );

        verify(repository)
                .findAll(any(Specification.class));
    }

    @Test
    void index_ShouldReturnMatchingVehicles_WhenFilterIsPopulated() {

        VehicleFilterDto filter =
                new VehicleFilterDto();

        filter.setModello("Lince");
        filter.setStato(status);
        filter.setVehicleCategoryId(1);
        filter.setBaseId(1);

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(vehicle));

        List<VehicleDto> result =
                service.index(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                "Iveco Lince",
                result.getFirst().getModello()
        );
        assertEquals(
                status,
                result.getFirst().getStato()
        );

        verify(repository)
                .findAll(any(Specification.class));
    }

    @Test
    void index_ShouldReturnEmptyList_WhenNoVehiclesMatch() {

        VehicleFilterDto filter =
                new VehicleFilterDto();

        filter.setModello(
                "Modello inesistente"
        );

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of());

        List<VehicleDto> result =
                service.index(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository)
                .findAll(any(Specification.class));
    }
}