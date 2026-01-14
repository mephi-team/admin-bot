package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import team.mephi.adminbot.dto.CohortDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для CohortServiceImpl.
 * Покрывают: выдачу и поиск списка потоков.
 */
class CohortServiceImplTest {

    /**
     * Проверяет возврат полного списка потоков.
     */
    @Test
    void Given_service_When_getAllCohorts_Then_returnsConfiguredList() {
        // Arrange
        CohortServiceImpl service = new CohortServiceImpl();

        // Act
        List<CohortDto> result = service.getAllCohorts();

        // Assert
        assertEquals(5, result.size());
        assertEquals("Весна 2026", result.getFirst().getName());
    }

    /**
     * Проверяет выдачу списка потоков с пагинацией.
     */
    @Test
    void Given_pageable_When_getAllCohortsPaged_Then_returnsList() {
        // Arrange
        CohortServiceImpl service = new CohortServiceImpl();

        // Act
        List<CohortDto> result = service.getAllCohorts(PageRequest.of(0, 2), "");

        // Assert
        assertEquals(5, result.size());
    }

    /**
     * Проверяет поиск потока по идентификатору.
     */
    @Test
    void Given_id_When_getById_Then_returnsCohort() {
        // Arrange
        CohortServiceImpl service = new CohortServiceImpl();

        // Act
        var result = service.getById("4e62388e-6e82-4ea2-aa0f-5571661c7358");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Весна 2026", result.get().getName());
    }

    /**
     * Проверяет поиск потока по имени.
     */
    @Test
    void Given_name_When_getByName_Then_returnsCohort() {
        // Arrange
        CohortServiceImpl service = new CohortServiceImpl();

        // Act
        var result = service.getByName("Зима 2025");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("d7f480c7-5e1d-4652-91de-82b9bde324d4", result.get().getId());
    }
}
