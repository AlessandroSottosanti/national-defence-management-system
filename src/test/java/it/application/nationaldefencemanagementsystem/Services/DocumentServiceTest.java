package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.DocumentsDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.DocumentFilterDto;
import it.application.nationaldefencemanagementsystem.Entities.Documents;
import it.application.nationaldefencemanagementsystem.Entities.Operator;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle; // Assicurati che il package sia corretto
import it.application.nationaldefencemanagementsystem.Mappers.DocumentMapper;
import it.application.nationaldefencemanagementsystem.Repositories.DocumentRepository;
import it.application.nationaldefencemanagementsystem.Repositories.OperatorRepository;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock private DocumentRepository repository;
    @Mock private OperatorRepository operatorRepository;
    @Mock private VehicleRepository vehicleRepository;
    @Mock private DocumentMapper mapper;

    @InjectMocks private DocumentService service;

    // Variabili condivise
    private DocumentsDto inputDto;
    private Documents entity;
    private Documents savedEntity;
    private DocumentsDto outputDto;
    private Operator mockOperator;
    private Vehicle mockVehicle;

    @BeforeEach
    void setUp() {
        inputDto = new DocumentsDto();
        entity = new Documents();
        savedEntity = new Documents();
        outputDto = new DocumentsDto();

        mockOperator = new Operator();
        mockOperator.setId(1);

        mockVehicle = new Vehicle();
        // Presupponendo che Vehicle abbia un setId()
         mockVehicle.setId(10);
    }

    // ==========================================
    // TEST PER IL METODO INSERT
    // ==========================================

    @Test
    void insert_ShouldSaveWithoutRelations_WhenIdsAreNull() {
        // Arrange
        inputDto.setOperatorId(null);
        inputDto.setVehicleId(null);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(outputDto);

        // Act
        DocumentsDto result = service.insert(inputDto);

        // Assert
        assertNotNull(result);
        verify(operatorRepository, never()).findById(any());
        verify(vehicleRepository, never()).findById(any());
        verify(repository).save(entity);
    }

    @Test
    void insert_ShouldAssignRelationsAndSave_WhenIdsAreValid() {
        // Arrange
        inputDto.setOperatorId(1);
        inputDto.setVehicleId(10);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(operatorRepository.findById(1)).thenReturn(Optional.of(mockOperator));
        when(vehicleRepository.findById(10)).thenReturn(Optional.of(mockVehicle));
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(outputDto);

        // Act
        DocumentsDto result = service.insert(inputDto);

        // Assert
        assertNotNull(result);
        verify(operatorRepository).findById(1);
        verify(vehicleRepository).findById(10);
        verify(repository).save(entity);
    }

    @Test
    void insert_ShouldThrowException_WhenOperatorNotFound() {
        // Arrange
        inputDto.setOperatorId(99);
        inputDto.setVehicleId(10);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(operatorRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.insert(inputDto);
        });

        assertEquals("Operator not found with id: 99", exception.getMessage());
        verify(vehicleRepository, never()).findById(any()); // Si ferma prima del veicolo
        verify(repository, never()).save(any());
    }

    @Test
    void insert_ShouldThrowException_WhenVehicleNotFound() {
        // Arrange
        inputDto.setOperatorId(1);
        inputDto.setVehicleId(88);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(operatorRepository.findById(1)).thenReturn(Optional.of(mockOperator));
        when(vehicleRepository.findById(88)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.insert(inputDto);
        });

        assertEquals("Vehicle not found with id: 88", exception.getMessage());
        verify(repository, never()).save(any());
    }

    // ==========================================
    // TEST PER IL METODO UPDATE
    // ==========================================

    @Test
    void update_ShouldSetRelationsToNull_WhenIdsAreNull() {
        // Arrange
        inputDto.setOperatorId(null);
        inputDto.setVehicleId(null);

        // Simuliamo un'entità che aveva precedentemente relazioni assegnate
        entity.setOperator(new Operator());
        entity.setVehicle(new Vehicle());

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(outputDto);

        // Act
        DocumentsDto result = service.update(inputDto);

        // Assert
        assertNotNull(result);
        // Verifichiamo che i vecchi valori siano stati azzerati (else block)
        assertNull(entity.getOperator());
        assertNull(entity.getVehicle());
        verify(operatorRepository, never()).findById(any());
        verify(vehicleRepository, never()).findById(any());
        verify(repository).save(entity);
    }

    @Test
    void update_ShouldAssignRelationsAndSave_WhenIdsAreValid() {
        // Arrange
        inputDto.setOperatorId(1);
        inputDto.setVehicleId(10);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(operatorRepository.findById(1)).thenReturn(Optional.of(mockOperator));
        when(vehicleRepository.findById(10)).thenReturn(Optional.of(mockVehicle));
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(outputDto);

        // Act
        DocumentsDto result = service.update(inputDto);

        // Assert
        assertNotNull(result);
        verify(operatorRepository).findById(1);
        verify(vehicleRepository).findById(10);
        verify(repository).save(entity);
    }

    @Test
    void update_ShouldThrowException_WhenOperatorNotFound() {
        // Arrange
        inputDto.setOperatorId(99);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(operatorRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.update(inputDto);
        });

        assertEquals("Operator not found with id: 99", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void update_ShouldThrowException_WhenVehicleNotFound() {
        // Arrange
        inputDto.setOperatorId(null); // Passa il controllo operatore (entra nell'else)
        inputDto.setVehicleId(88); // Va in crash qui

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(vehicleRepository.findById(88)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.update(inputDto);
        });

        assertEquals("Vehicle not found with id: 88", exception.getMessage());
        verify(repository, never()).save(any());
    }

    // ==========================================
    // TEST PER IL METODO INDEX (FILTRI)
    // ==========================================

    @Test
    void index_ShouldReturnEmptyList_WhenNoDocumentsMatch() {
        // Arrange
        DocumentFilterDto filter = new DocumentFilterDto();
        filter.setTitle("DocumentoSegreto");

        when(repository.findAll(any(Specification.class))).thenReturn(List.of());

        // Act
        List<DocumentsDto> result = service.index(filter);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAll(any(Specification.class));
    }

    @Test
    void index_ShouldReturnFilteredList_WhenFilterHasCriteria() {
        // Arrange
        DocumentFilterDto filter = new DocumentFilterDto();
        filter.setTitle("Manuale");
        filter.setOperatorFirstName("Mario");
        filter.setVehicleId(5);

        List<Documents> mockEntities = List.of(entity);
        when(repository.findAll(any(Specification.class))).thenReturn(mockEntities);

        // Act
        List<DocumentsDto> result = service.index(filter);

        // Assert
        assertNotNull(result);
        verify(repository).findAll(any(Specification.class));
    }
}