package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.OperatorFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.OperatorDto;
import it.application.nationaldefencemanagementsystem.Entities.Base;
import it.application.nationaldefencemanagementsystem.Entities.Operator;
import it.application.nationaldefencemanagementsystem.Entities.OperatorStatus;
import it.application.nationaldefencemanagementsystem.Mappers.OperatorMapper;
import it.application.nationaldefencemanagementsystem.Repositories.BaseRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperatorServiceTest {

    @Mock
    private OperatorRepository repository;

    @Mock
    private BaseRepository baseRepository;

    private OperatorMapper mapper;

    @InjectMocks
    private OperatorService service;

    private Operator operator;
    private OperatorDto dto;
    private Base base;

    @BeforeEach
    void setUp() {

        mapper = new OperatorMapper();

        service = new OperatorService(
                repository,
                mapper,
                baseRepository
        );

        base = new Base();
        base.setId(1);

        operator = new Operator();
        operator.setId(1);
        operator.setServiceNumber("OP001");
        operator.setFirstName("Mario");
        operator.setLastName("Rossi");
        operator.setRank("Captain");
        operator.setHeightInCm(180);
        operator.setWeightInKg(80);
        operator.setStatus(OperatorStatus.ACTIVE);
        operator.setBase(base);

        dto = new OperatorDto();
        dto.setId(1);
        dto.setServiceNumber("OP001");
        dto.setFirstName("Mario");
        dto.setLastName("Rossi");
        dto.setRank("Captain");
        dto.setHeightInCm(180);
        dto.setWeightInKg(80);
        dto.setStatus(OperatorStatus.ACTIVE);
        dto.setBaseId(1);
    }

    @Test
    void insert_ShouldSaveOperator_WhenBaseExists() {

        when(baseRepository.findById(1))
                .thenReturn(Optional.of(base));

        when(repository.save(any(Operator.class)))
                .thenReturn(operator);

        OperatorDto result =
                service.insert(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());

        verify(baseRepository).findById(1);
        verify(repository).save(any(Operator.class));
    }

    @Test
    void insert_ShouldThrowException_WhenBaseNotFound() {

        when(baseRepository.findById(1))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.insert(dto)
                );

        assertEquals(
                "Base not found with id: 1",
                ex.getMessage()
        );

        verify(repository, never())
                .save(any());
    }

    @Test
    void update_ShouldSaveOperator_WhenBaseExists() {

        when(baseRepository.findById(1))
                .thenReturn(Optional.of(base));

        when(repository.save(any(Operator.class)))
                .thenReturn(operator);

        OperatorDto result =
                service.update(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());

        verify(baseRepository).findById(1);
        verify(repository).save(any(Operator.class));
    }

    @Test
    void update_ShouldThrowException_WhenBaseNotFound() {

        when(baseRepository.findById(1))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.update(dto)
                );

        assertEquals(
                "Base not found with id: 1",
                ex.getMessage()
        );

        verify(repository, never())
                .save(any());
    }

    @Test
    void index_ShouldReturnAllOperators_WhenFilterIsEmpty() {

        OperatorFilterDto filter =
                new OperatorFilterDto();

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(operator));

        List<OperatorDto> result =
                service.index(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mario", result.getFirst().getFirstName());

        verify(repository)
                .findAll(any(Specification.class));
    }

    @Test
    void index_ShouldReturnMatchingOperators_WhenFilterIsPopulated() {

        OperatorFilterDto filter =
                new OperatorFilterDto();

        filter.setFirstName("Mario");

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(operator));

        List<OperatorDto> result =
                service.index(filter);

        assertEquals(1, result.size());
        assertEquals("Mario", result.getFirst().getFirstName());

        verify(repository)
                .findAll(any(Specification.class));
    }

    @Test
    void index_ShouldReturnEmptyList_WhenNoOperatorsMatch() {

        OperatorFilterDto filter =
                new OperatorFilterDto();

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of());

        List<OperatorDto> result =
                service.index(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository)
                .findAll(any(Specification.class));
    }
}