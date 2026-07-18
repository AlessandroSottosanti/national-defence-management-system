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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceTest {

    // Repository principale delle manutenzioni.

    @Mock
    private MaintenanceRepository repository;

    // Repository degli elementi collegabili alla manutenzione.

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    // Mapper reale.

    private MaintenanceMapper mapper;

    // Service testato.

    private MaintenanceService service;

    // Dati comuni.

    private Maintenance maintenance;
    private MaintenanceDto dto;
    private Vehicle vehicle;
    private Equipment equipment;

    @BeforeEach
    void setUp() {

        mapper = new MaintenanceMapper();

        service = new MaintenanceService(
                repository,
                mapper,
                vehicleRepository,
                equipmentRepository
        );

        vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setModello("Iveco Lince");

        equipment = new Equipment();
        equipment.setId(2);
        equipment.setName("Sistema radio");

        maintenance = new Maintenance();
        maintenance.setId(1);
        maintenance.setVehicle(vehicle);
        maintenance.setEquipment(null);
        maintenance.setDescription(
                "Controllo completo del motore"
        );
        maintenance.setStartDate(
                LocalDate.of(2026, 7, 16)
        );
        maintenance.setEndDate(
                LocalDate.of(2026, 7, 20)
        );
        maintenance.setEstimatedMaintenanceDays(4);
        maintenance.setCost(
                new BigDecimal("1500.00")
        );

        dto = new MaintenanceDto();
        dto.setId(1);
        dto.setVehicleId(1);
        dto.setEquipmentId(null);
        dto.setDescription(
                "Controllo completo del motore"
        );
        dto.setStartDate(
                LocalDate.of(2026, 7, 16)
        );
        dto.setEndDate(
                LocalDate.of(2026, 7, 20)
        );
        dto.setEstimatedMaintenanceDays(4);
        dto.setCost(
                new BigDecimal("1500.00")
        );
    }

    // =========================================================
    // INSERT
    // =========================================================

    @Test
    void insert_ShouldSaveMaintenance_WhenVehicleExists() {

        when(vehicleRepository.findById(1))
                .thenReturn(Optional.of(vehicle));

        when(repository.save(any(Maintenance.class)))
                .thenReturn(maintenance);

        MaintenanceDto result =
                service.insert(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(
                1,
                result.getVehicleId()
        );
        assertNull(result.getEquipmentId());
        assertEquals(
                "Controllo completo del motore",
                result.getDescription()
        );

        verify(vehicleRepository)
                .findById(1);

        verify(repository)
                .save(any(Maintenance.class));
    }

    @Test
    void insert_ShouldSaveMaintenance_WhenEquipmentExists() {

        // In questo caso la manutenzione riguarda un equipaggiamento.

        dto.setVehicleId(null);
        dto.setEquipmentId(2);

        maintenance.setVehicle(null);
        maintenance.setEquipment(equipment);

        when(equipmentRepository.findById(2))
                .thenReturn(Optional.of(equipment));

        when(repository.save(any(Maintenance.class)))
                .thenReturn(maintenance);

        MaintenanceDto result =
                service.insert(dto);

        assertNotNull(result);
        assertNull(result.getVehicleId());
        assertEquals(
                2,
                result.getEquipmentId()
        );

        verify(equipmentRepository)
                .findById(2);

        verify(repository)
                .save(any(Maintenance.class));
    }

    @Test
    void insert_ShouldThrowException_WhenVehicleNotFound() {

        when(vehicleRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.insert(dto)
        );

        verify(repository, never())
                .save(any(Maintenance.class));
    }

    @Test
    void insert_ShouldThrowException_WhenEquipmentNotFound() {

        dto.setVehicleId(null);
        dto.setEquipmentId(2);

        when(equipmentRepository.findById(2))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.insert(dto)
        );

        verify(repository, never())
                .save(any(Maintenance.class));
    }

    @Test
    void insert_ShouldThrowException_WhenNoTargetIsProvided() {

        /*
         * Una manutenzione deve avere un solo obiettivo:
         * un veicolo oppure un equipaggiamento.
         */

        dto.setVehicleId(null);
        dto.setEquipmentId(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.insert(dto)
        );

        verify(repository, never())
                .save(any(Maintenance.class));
    }

    @Test
    void insert_ShouldThrowException_WhenBothTargetsAreProvided() {

        dto.setVehicleId(1);
        dto.setEquipmentId(2);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.insert(dto)
        );

        verify(repository, never())
                .save(any(Maintenance.class));
    }

    @Test
    void insert_ShouldThrowException_WhenEndDateIsBeforeStartDate() {

        dto.setStartDate(
                LocalDate.of(2026, 7, 20)
        );

        dto.setEndDate(
                LocalDate.of(2026, 7, 16)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.insert(dto)
        );

        verify(repository, never())
                .save(any(Maintenance.class));
    }

    // =========================================================
    // READ
    // =========================================================

    @Test
    void read_ShouldReturnMaintenance_WhenIdExists() {

        when(repository.findById(1))
                .thenReturn(Optional.of(maintenance));

        MaintenanceDto result =
                service.read(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(
                "Controllo completo del motore",
                result.getDescription()
        );
        assertEquals(
                1,
                result.getVehicleId()
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void update_ShouldSaveMaintenance_WhenVehicleExists() {

        dto.setDescription(
                "Controllo motore aggiornato"
        );

        when(repository.findById(1))
                .thenReturn(Optional.of(maintenance));

        when(vehicleRepository.findById(1))
                .thenReturn(Optional.of(vehicle));

        when(repository.save(any(Maintenance.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MaintenanceDto result =
                service.update(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(
                "Controllo motore aggiornato",
                result.getDescription()
        );
        assertEquals(
                1,
                result.getVehicleId()
        );

        verify(repository)
                .findById(1);

        verify(vehicleRepository)
                .findById(1);

        verify(repository)
                .save(any(Maintenance.class));
    }

    @Test
    void update_ShouldSaveMaintenance_WhenEquipmentExists() {

        dto.setVehicleId(null);
        dto.setEquipmentId(2);

        when(repository.findById(1))
                .thenReturn(Optional.of(maintenance));

        when(equipmentRepository.findById(2))
                .thenReturn(Optional.of(equipment));

        when(repository.save(any(Maintenance.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MaintenanceDto result =
                service.update(dto);

        assertNotNull(result);
        assertNull(result.getVehicleId());
        assertEquals(
                2,
                result.getEquipmentId()
        );

        verify(repository)
                .findById(1);

        verify(equipmentRepository)
                .findById(2);

        verify(repository)
                .save(any(Maintenance.class));
    }

    @Test
    void update_ShouldThrowException_WhenIdIsNull() {

        dto.setId(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.update(dto)
        );

        verify(repository, never())
                .save(any(Maintenance.class));
    }

    @Test
    void update_ShouldThrowException_WhenMaintenanceNotFound() {

        dto.setId(99);

        when(repository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.update(dto)
        );

        verify(repository, never())
                .save(any(Maintenance.class));
    }

    @Test
    void update_ShouldThrowException_WhenEndDateIsBeforeStartDate() {

        dto.setStartDate(
                LocalDate.of(2026, 7, 20)
        );

        dto.setEndDate(
                LocalDate.of(2026, 7, 16)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.update(dto)
        );

        verify(repository, never())
                .save(any(Maintenance.class));
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void delete_ShouldDeleteMaintenance() {

        service.delete(1);

        verify(repository)
                .deleteById(1);
    }

    // =========================================================
    // INDEX
    // =========================================================

    @Test
    void index_ShouldReturnAllMaintenances_WhenFilterIsEmpty() {

        MaintenanceFilterDto filter =
                new MaintenanceFilterDto();

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(maintenance));

        List<MaintenanceDto> result =
                service.index(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                "Controllo completo del motore",
                result.getFirst().getDescription()
        );

        verify(repository)
                .findAll(any(Specification.class));
    }

    @Test
    void index_ShouldReturnMatchingMaintenances_WhenFilterIsPopulated() {

        MaintenanceFilterDto filter =
                new MaintenanceFilterDto();

        filter.setVehicleId(1);
        filter.setDescription("motore");
        filter.setStartDate(
                LocalDate.of(2026, 7, 16)
        );
        filter.setEndDate(
                LocalDate.of(2026, 7, 20)
        );
        filter.setEstimatedMaintenanceDays(4);
        filter.setCost(
                new BigDecimal("1500.00")
        );

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(maintenance));

        List<MaintenanceDto> result =
                service.index(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                "Controllo completo del motore",
                result.getFirst().getDescription()
        );
        assertEquals(
                new BigDecimal("1500.00"),
                result.getFirst().getCost()
        );

        verify(repository)
                .findAll(any(Specification.class));
    }

    @Test
    void index_ShouldReturnEmptyList_WhenNoMaintenancesMatch() {

        MaintenanceFilterDto filter =
                new MaintenanceFilterDto();

        filter.setDescription(
                "Descrizione inesistente"
        );

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of());

        List<MaintenanceDto> result =
                service.index(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository)
                .findAll(any(Specification.class));
    }
}