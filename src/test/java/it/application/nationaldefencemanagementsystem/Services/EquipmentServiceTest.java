package it.application.nationaldefencemanagementsystem.Services;


import it.application.nationaldefencemanagementsystem.DTOs.EquipmentDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.EquipmentFilterDto;
import it.application.nationaldefencemanagementsystem.Entities.Equipment;
import it.application.nationaldefencemanagementsystem.Entities.Operator;
import it.application.nationaldefencemanagementsystem.Mappers.Converter;
import it.application.nationaldefencemanagementsystem.Mappers.EquipmentMapper;
import it.application.nationaldefencemanagementsystem.Repositories.EquipmentRepository;
import it.application.nationaldefencemanagementsystem.Repositories.OperatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTest {

    @Mock
    private EquipmentRepository repository;

    @Mock
    private EquipmentMapper mapper;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private Converter<Equipment, EquipmentDto> converter;

    @InjectMocks
    private EquipmentService service;

    private EquipmentDto inputDto;
    private Equipment entity;
    private Equipment savedEntity;
    private EquipmentDto outputDto;
    private Operator mockOperator;

    @BeforeEach
    void setUp() {
        inputDto = new EquipmentDto();

        entity = new Equipment();

        savedEntity = new Equipment();

        outputDto = new EquipmentDto();

        mockOperator = new Operator();
        mockOperator.setId(1);
    }

    @Test
    void insert_ShouldHaveEquipment_whenOperatorExists() {
        inputDto.setOperatorId(1);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(operatorRepository.findById(1)).thenReturn(Optional.of(mockOperator));
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(outputDto);

        // 2. Act
        EquipmentDto result = service.insert(inputDto); //[cite: 5]

        // 3. Assert
        assertNotNull(result);
        assertEquals(mockOperator, entity.getOperator()); //[cite: 1]
        verify(operatorRepository).findById(1);
        verify(repository).save(entity);
    }

    @Test
    void insert_ShouldNotSearchOperator_whenOperatorIdIsNull(){
        inputDto.setOperatorId(null);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(outputDto);

        EquipmentDto result = service.insert(inputDto);

        assertNotNull(result);
        assertNull(entity.getOperator());
        verify(operatorRepository, never()).findById(any()); // Il DB non deve essere interrogato
        verify(repository).save(entity);
    }

    // ID <= 0
    @Test
    void insert_ShouldSaveWithoutOperator_WhenOperatorIdIsZeroOrNegative() {
        // 1. Arrange
        // Il service verifica se l'id è > 0. Se è 0 o negativo, lo ignora.
        inputDto.setOperatorId(0);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(outputDto);

        // 2. Act
        EquipmentDto result = service.insert(inputDto);

        // 3. Assert
        assertNotNull(result);
        assertNull(entity.getOperator());
        verify(operatorRepository, never()).findById(any());
        verify(repository).save(entity);
    }

    @Test
    void insert_ShouldThrowException_WhenOperatorNotFound() {
        inputDto.setOperatorId(999);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(operatorRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.insert(inputDto);
        });

        assertEquals("Operator not found with id: 999", exception.getMessage());

        verify(repository, never()).save(any());
    }

    void update_ShouldUpdateEquipment(){
        inputDto.setOperatorId(1);

        when(mapper.toEntity(inputDto)).thenReturn(entity);

        when(operatorRepository.findById(1)).
                thenReturn(Optional.of(mockOperator));

        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(outputDto);

        EquipmentDto result = service.update(inputDto);

        assertNotNull(result);
        assertEquals(mockOperator,entity.getOperator());
        verify(operatorRepository).findById(1);
        verify(repository).save(entity);

    }

    @Test
    void update_ShouldSetOperatorToNull_WhenOperatorIdIsNull(){
        inputDto.setOperatorId(null);

        //Simuliamo che l'entità avesse precedentemente un operatore assegnato
        entity.setOperator(new Operator());
        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(outputDto);

        EquipmentDto result = service.update(inputDto);
        assertNotNull(result);
        assertNull(entity.getOperator()); // Verifichiamo che l'operatore sia stato azzerato
        verify(operatorRepository, never()).findById(any()); // Nessuna ricerca su DB
        verify(repository).save(entity);

    }

    @Test
    void update_ShouldThrowException_WhenOperatorNotFound() {
        inputDto.setOperatorId(999);

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(operatorRepository.findById(999)).thenReturn(Optional.empty()); // DB non trova l'operatore

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.update(inputDto);
        });

        assertEquals("Operator not found with id: 999", exception.getMessage());
        verify(repository, never()).save(any()); // Il salvataggio viene bloccato
    }

    // --- CASO 1: Filtro vuoto (Ricerca globale) ---
    @Test
    void index_ShouldReturnEquipmentList_WhenFilterIsEmpty() {
        // 1. Arrange
        EquipmentFilterDto emptyFilter = new EquipmentFilterDto();

        // Creiamo una finta lista di risultati dal database
        List<Equipment> mockEntities = List.of(entity);

        // Istruiamo il finto DB a restituire la lista quando riceve una Specification qualsiasi
        when(repository.findAll(any(Specification.class))).thenReturn(mockEntities);

        // 2. Act
        List<EquipmentDto> result = service.index(emptyFilter);

        // 3. Assert
        assertNotNull(result);
        verify(repository).findAll(any(Specification.class)); // Verifichiamo che il DB sia stato interrogato
    }

    // --- CASO 2: Filtro popolato (Ricerca specifica) ---
    @Test
    void index_ShouldReturnFilteredEquipmentList_WhenFilterHasCriteria() {
        // 1. Arrange
        EquipmentFilterDto populatedFilter = new EquipmentFilterDto();
        populatedFilter.setName("Beretta");
        populatedFilter.setFireArm(true);
        populatedFilter.setAmmunitionType("9x19mm Parabellum");
        populatedFilter.setOperatorId(1);

        List<Equipment> mockEntities = List.of(entity);

        when(repository.findAll(any(Specification.class))).thenReturn(mockEntities);

        // 2. Act
        List<EquipmentDto> result = service.index(populatedFilter);

        // 3. Assert
        assertNotNull(result);
        // Verifichiamo che la chiamata al DB sia avvenuta correttamente con i filtri applicati
        verify(repository).findAll(any(Specification.class));
    }





}
