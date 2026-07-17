package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.ArmedForceDto;
import it.application.nationaldefencemanagementsystem.Entities.ArmedForce;
import it.application.nationaldefencemanagementsystem.Mappers.ArmedForceMapper;
import it.application.nationaldefencemanagementsystem.Repositories.ArmedForceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArmedForceServiceTest {

    @Mock
    private ArmedForceRepository repository;

    private ArmedForceMapper mapper;

    private ArmedForceService service;

    private ArmedForce armedForce;

    @BeforeEach
    void setUp() {

        mapper = new ArmedForceMapper();

        service =
                new ArmedForceService(
                        repository,
                        mapper
                );

        armedForce = new ArmedForce();

        armedForce.setId(1);
        armedForce.setName("Army");
    }

    @Test
    void index_ShouldReturnAllArmedForces_WhenNameIsNull() {

        when(repository.findAll())
                .thenReturn(List.of(armedForce));

        List<ArmedForceDto> result =
                service.index(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                "Army",
                result.getFirst().getName()
        );

        verify(repository)
                .findAll();

        verify(repository, never())
                .findByNameContainingIgnoreCase(anyString());
    }

    @Test
    void index_ShouldReturnAllArmedForces_WhenNameIsBlank() {

        when(repository.findAll())
                .thenReturn(List.of(armedForce));

        List<ArmedForceDto> result =
                service.index(" ");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                "Army",
                result.getFirst().getName()
        );

        verify(repository)
                .findAll();

        verify(repository, never())
                .findByNameContainingIgnoreCase(anyString());
    }

    @Test
    void index_ShouldReturnMatchingArmedForces_WhenNameProvided() {

        when(repository.findByNameContainingIgnoreCase("arm"))
                .thenReturn(List.of(armedForce));

        List<ArmedForceDto> result =
                service.index("arm");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                "Army",
                result.getFirst().getName()
        );

        verify(repository, never())
                .findAll();

        verify(repository)
                .findByNameContainingIgnoreCase("arm");
    }
}