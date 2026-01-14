package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.model.Direction;
import team.mephi.adminbot.repository.DirectionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для DirectionServiceImpl.
 * Покрывают: инициализацию списка направлений и поиск.
 */
@ExtendWith(MockitoExtension.class)
class DirectionServiceImplTest {
    @Mock
    private DirectionRepository directionRepository;

    /**
     * Проверяет инициализацию списка направлений.
     */
    @Test
    void Given_repositoryDirections_When_getAllDirections_Then_includesAllOption() {
        // Arrange
        Direction direction = Direction.builder().id(2L).name("Math").build();
        when(directionRepository.findAll()).thenReturn(List.of(direction));
        DirectionServiceImpl service = new DirectionServiceImpl(directionRepository);

        // Act
        List<SimpleDirection> result = service.getAllDirections();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Все", result.getFirst().getName());
    }

    /**
     * Проверяет получение списка направлений с пагинацией.
     */
    @Test
    void Given_pageable_When_getAllDirectionsPaged_Then_returnsList() {
        // Arrange
        Direction direction = Direction.builder().id(3L).name("Physics").build();
        when(directionRepository.findAll()).thenReturn(List.of(direction));
        DirectionServiceImpl service = new DirectionServiceImpl(directionRepository);

        // Act
        List<SimpleDirection> result = service.getAllDirections(PageRequest.of(0, 2), "");

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Проверяет подсчёт направлений.
     */
    @Test
    void Given_query_When_countAllDirections_Then_countsInitializedList() {
        // Arrange
        Direction direction = Direction.builder().id(1L).name("Math").build();
        when(directionRepository.findAll()).thenReturn(List.of(direction));
        DirectionServiceImpl service = new DirectionServiceImpl(directionRepository);

        // Act
        Integer result = service.countAllDirections("q");

        // Assert
        assertEquals(2, result);
    }

    /**
     * Проверяет возврат пустого результата при null идентификаторе.
     */
    @Test
    void Given_nullId_When_getById_Then_returnsEmpty() {
        // Arrange
        DirectionServiceImpl service = new DirectionServiceImpl(directionRepository);

        // Act
        Optional<SimpleDirection> result = service.getById(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    /**
     * Проверяет поиск направления по идентификатору.
     */
    @Test
    void Given_id_When_getById_Then_mapsDirection() {
        // Arrange
        Direction direction = Direction.builder().id(4L).name("Chemistry").build();
        when(directionRepository.findById(eq(4L))).thenReturn(Optional.of(direction));
        DirectionServiceImpl service = new DirectionServiceImpl(directionRepository);

        // Act
        Optional<SimpleDirection> result = service.getById(4L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Chemistry", result.get().getName());
    }

    /**
     * Проверяет поиск направления по имени.
     */
    @Test
    void Given_name_When_getByName_Then_returnsMatch() {
        // Arrange
        Direction direction = Direction.builder().id(5L).name("Math").build();
        when(directionRepository.findAll()).thenReturn(List.of(direction));
        DirectionServiceImpl service = new DirectionServiceImpl(directionRepository);

        // Act
        Optional<SimpleDirection> result = service.getByName("Math");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getId());
    }
}
