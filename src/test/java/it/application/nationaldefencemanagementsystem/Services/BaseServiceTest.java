package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.BaseDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.BaseFilterDto;
import it.application.nationaldefencemanagementsystem.Entities.ArmedForce;
import it.application.nationaldefencemanagementsystem.Entities.Base;
import it.application.nationaldefencemanagementsystem.Mappers.BaseMapper;
import it.application.nationaldefencemanagementsystem.Repositories.ArmedForceRepository;
import it.application.nationaldefencemanagementsystem.Repositories.BaseRepository;
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
class BaseServiceTest {

    @Mock
    private BaseRepository repository;

    @Mock
    private ArmedForceRepository armedForceRepository;

    private BaseMapper mapper;

    @InjectMocks
    private BaseService service;

    private Base base;
    private BaseDto dto;
    private ArmedForce armedForce;

    @BeforeEach
    void setUp() {

        mapper = new BaseMapper();

        service =
                new BaseService(
                        repository,
                        armedForceRepository,
                        mapper
                );

        armedForce = new ArmedForce();
        armedForce.setId(1);
        armedForce.setName("Army");

        base = new Base();
        base.setId(1);
        base.setName("Base Alpha");
        base.setCity("Rome");
        base.setAddress("Via Roma");
        base.setArmedForce(armedForce);

        dto = new BaseDto();
        dto.setId(1);
        dto.setName("Base Alpha");
        dto.setCity("Rome");
        dto.setAddress("Via Roma");
        dto.setArmedForceId(1);
    }

    // INSERT

    @Test
    void insert_ShouldSaveBase_WhenArmedForceExists() {

        when(armedForceRepository.findById(1))
                .thenReturn(Optional.of(armedForce));

        when(repository.save(any(Base.class)))
                .thenReturn(base);

        BaseDto result =
                service.insert(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());

        verify(armedForceRepository)
                .findById(1);

        verify(repository)
                .save(any(Base.class));
    }

    @Test
    void insert_ShouldThrowException_WhenArmedForceNotFound() {

        when(armedForceRepository.findById(1))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.insert(dto)
                );

        assertEquals(
                "Armed Force not found with id: 1",
                ex.getMessage()
        );

        verify(repository, never())
                .save(any());
    }

    // UPDATE

    @Test
    void update_ShouldSaveBase_WhenArmedForceExists() {

        when(armedForceRepository.findById(1))
                .thenReturn(Optional.of(armedForce));

        when(repository.save(any(Base.class)))
                .thenReturn(base);

        BaseDto result =
                service.update(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());

        verify(armedForceRepository)
                .findById(1);

        verify(repository)
                .save(any(Base.class));
    }

    @Test
    void update_ShouldThrowException_WhenArmedForceNotFound() {

        when(armedForceRepository.findById(1))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.update(dto)
                );

        assertEquals(
                "Armed Force not found with id: 1",
                ex.getMessage()
        );

        verify(repository, never())
                .save(any());
    }

    // INDEX

    @Test
    void index_ShouldReturnAllBases_WhenFilterIsEmpty() {

        BaseFilterDto filter =
                new BaseFilterDto();

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(base));

        List<BaseDto> result =
                service.index(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                "Base Alpha",
                result.getFirst().getName()
        );

        verify(repository)
                .findAll(any(Specification.class));
    }

    @Test
    void index_ShouldReturnMatchingBases_WhenFilterIsPopulated() {

        BaseFilterDto filter =
                new BaseFilterDto();

        filter.setName("Alpha");

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(base));

        List<BaseDto> result =
                service.index(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                "Base Alpha",
                result.getFirst().getName()
        );

        verify(repository)
                .findAll(any(Specification.class));
    }

    @Test
    void index_ShouldReturnEmptyList_WhenNoBasesMatch() {

        BaseFilterDto filter =
                new BaseFilterDto();

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of());

        List<BaseDto> result =
                service.index(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository)
                .findAll(any(Specification.class));
    }
}