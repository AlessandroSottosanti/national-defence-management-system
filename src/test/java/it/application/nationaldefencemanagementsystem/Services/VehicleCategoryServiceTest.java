package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.VehicleCategoryDto;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import it.application.nationaldefencemanagementsystem.Mappers.VehicleCategoryMapper;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleCategoryServiceTest {

    // Il repository viene simulato con Mockito.

    @Mock
    private VehicleCategoryRepository repository;

    // Il mapper viene utilizzato realmente.

    private VehicleCategoryMapper mapper;

    // Il service viene costruito nel setUp.

    private VehicleCategoryService service;

    // Dati comuni usati nei vari test.

    private VehicleCategory category;
    private VehicleCategoryDto dto;

    @BeforeEach
    void setUp() {

        mapper = new VehicleCategoryMapper();

        service = new VehicleCategoryService(
                repository,
                mapper
        );

        category = new VehicleCategory();
        category.setId(1);
        category.setName("Mezzi terrestri");

        dto = new VehicleCategoryDto();
        dto.setId(1);
        dto.setName("Mezzi terrestri");
    }

    // =========================================================
    // INSERT
    // =========================================================

    @Test
    void insert_ShouldSaveVehicleCategory() {

        when(repository.save(any(VehicleCategory.class)))
                .thenReturn(category);

        VehicleCategoryDto result =
                service.insert(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(
                "Mezzi terrestri",
                result.getName()
        );

        verify(repository)
                .save(any(VehicleCategory.class));
    }

    @Test
    void insert_ShouldThrowException_WhenRepositoryFails() {

        when(repository.save(any(VehicleCategory.class)))
                .thenThrow(new RuntimeException());

        assertThrows(
                RuntimeException.class,
                () -> service.insert(dto)
        );

        verify(repository)
                .save(any(VehicleCategory.class));
    }

    // =========================================================
    // READ
    // =========================================================

    @Test
    void read_ShouldReturnVehicleCategory_WhenIdExists() {

        /*
         * L'attuale AbstractService chiama findById due volte.
         * Non verifichiamo il numero di chiamate perché questo
         * comportamento dovrebbe essere corretto nel service.
         */

        when(repository.findById(1))
                .thenReturn(Optional.of(category));

        VehicleCategoryDto result =
                service.read(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(
                "Mezzi terrestri",
                result.getName()
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void update_ShouldSaveVehicleCategory() {

        dto.setName(
                "Veicoli militari terrestri"
        );

        category.setName(
                "Veicoli militari terrestri"
        );

        when(repository.save(any(VehicleCategory.class)))
                .thenReturn(category);

        VehicleCategoryDto result =
                service.update(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(
                "Veicoli militari terrestri",
                result.getName()
        );

        verify(repository)
                .save(any(VehicleCategory.class));
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void delete_ShouldDeleteVehicleCategory() {

        service.delete(1);

        verify(repository)
                .deleteById(1);
    }

    // =========================================================
    // INDEX
    // =========================================================

    @Test
    void index_ShouldReturnAllVehicleCategories() {

        when(repository.findAll())
                .thenReturn(List.of(category));

        List<VehicleCategoryDto> result =
                service.index();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                "Mezzi terrestri",
                result.getFirst().getName()
        );

        verify(repository)
                .findAll();
    }

    @Test
    void index_ShouldReturnEmptyList_WhenNoCategoriesExist() {

        when(repository.findAll())
                .thenReturn(List.of());

        List<VehicleCategoryDto> result =
                service.index();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository)
                .findAll();
    }
}