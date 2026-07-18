package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.Mappers.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractServiceTest {

    @Mock
    private JpaRepository<TestEntity, Integer> repository;

    private TestConverter converter;
    private TestService service;

    private TestEntity entity;
    private TestDto dto;

    @BeforeEach
    void setUp() {

        converter = new TestConverter();

        service =
                new TestService(
                        repository,
                        converter
                );

        entity =
                new TestEntity(
                        1,
                        "Mario"
                );

        dto =
                new TestDto(
                        1,
                        "Mario"
                );
    }

    // INSERT

    @Test
    void insert_ShouldReturnSavedDto() {

        when(repository.save(any(TestEntity.class)))
                .thenReturn(entity);

        TestDto result =
                service.insert(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());

        verify(repository).save(any(TestEntity.class));
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenDtoIsNull() {

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.insert(null)
                );

        assertEquals(
                "Il DTO non può essere nullo",
                ex.getMessage()
        );

        verify(repository, never()).save(any());
    }

    @Test
    void insert_ShouldThrowRuntimeException_WhenSaveFails() {

        when(repository.save(any(TestEntity.class)))
                .thenThrow(new RuntimeException("DB Error"));

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.insert(dto)
                );

        assertEquals(
                "Errore durante il salvataggio",
                ex.getMessage()
        );
    }

    // READ

    @Test
    void read_ShouldReturnDto_WhenFound() {

        when(repository.findById(1))
                .thenReturn(Optional.of(entity));

        TestDto result =
                service.read(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Mario", result.getName());
    }

    @Test
    void read_ShouldThrowIllegalArgumentException_WhenIdIsNull() {

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.read(null)
                );

        assertEquals(
                "L'id non può essere nullo",
                ex.getMessage()
        );
    }

    @Test
    void read_ShouldThrowNoSuchElementException_WhenNotFound() {

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        NoSuchElementException ex =
                assertThrows(
                        NoSuchElementException.class,
                        () -> service.read(1)
                );

        assertEquals(
                "Entità non trovata con id: 1",
                ex.getMessage()
        );
    }

    // UPDATE

    @Test
    void update_ShouldReturnUpdatedDto() {

        when(repository.save(any(TestEntity.class)))
                .thenReturn(entity);

        TestDto result =
                service.update(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());

        verify(repository).save(any(TestEntity.class));
    }

    // DELETE

    @Test
    void delete_ShouldDeleteEntity() {

        when(repository.existsById(1))
                .thenReturn(true);

        service.delete(1);

        verify(repository).deleteById(1);
    }

    @Test
    void delete_ShouldThrowIllegalArgumentException_WhenIdIsNull() {

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.delete(null)
                );

        assertEquals(
                "L'id non può essere nullo",
                ex.getMessage()
        );
    }

    @Test
    void delete_ShouldThrowNoSuchElementException_WhenEntityNotFound() {

        when(repository.existsById(1))
                .thenReturn(false);

        NoSuchElementException ex =
                assertThrows(
                        NoSuchElementException.class,
                        () -> service.delete(1)
                );

        assertEquals(
                "Entità non trovata con id: 1",
                ex.getMessage()
        );

        verify(repository, never()).deleteById(any());
    }

    // CLASSI DI SUPPORTO

    static class TestEntity {

        private Integer id;
        private String name;

        public TestEntity(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    static class TestDto {

        private Integer id;
        private String name;

        public TestDto(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    static class TestConverter implements Converter<TestEntity, TestDto> {

        @Override
        public TestDto toDTO(TestEntity entity) {

            return new TestDto(
                    entity.getId(),
                    entity.getName()
            );
        }

        @Override
        public TestEntity toEntity(TestDto dto) {

            return new TestEntity(
                    dto.getId(),
                    dto.getName()
            );
        }

        @Override
        public List<TestDto> toDTOList(Iterable<TestEntity> entities) {

            List<TestDto> result = new ArrayList<>();

            for (TestEntity entity : entities) {

                result.add(
                        toDTO(entity)
                );
            }

            return result;
        }

        @Override
        public List<TestEntity> toEntityList(Iterable<TestDto> dtoList) {

            List<TestEntity> result = new ArrayList<>();

            for (TestDto dto : dtoList) {

                result.add(
                        toEntity(dto)
                );
            }

            return result;
        }
    }

    static class TestService
            extends AbstractService<TestEntity, TestDto> {

        protected TestService(
                JpaRepository<TestEntity, Integer> repository,
                Converter<TestEntity, TestDto> converter) {

            super(repository, converter);
        }
    }
}