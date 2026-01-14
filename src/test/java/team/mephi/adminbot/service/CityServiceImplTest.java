package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import team.mephi.adminbot.dto.CityDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для CityServiceImpl.
 * Покрывают: выдачу городов и поиск по идентификаторам.
 */
class CityServiceImplTest {

    /**
     * Проверяет возврат полного списка городов.
     */
    @Test
    void Given_service_When_getAllCities_Then_returnsConfiguredList() {
        // Arrange
        CityServiceImpl service = new CityServiceImpl();

        // Act
        List<CityDto> result = service.getAllCities();

        // Assert
        assertEquals(4, result.size());
        assertEquals("Все", result.getFirst().getName());
    }

    /**
     * Проверяет возврат списка городов с пагинацией.
     */
    @Test
    void Given_pageable_When_getAllCitiesWithPage_Then_returnsConfiguredList() {
        // Arrange
        CityServiceImpl service = new CityServiceImpl();

        // Act
        List<CityDto> result = service.getAllCities(PageRequest.of(0, 2), "");

        // Assert
        assertEquals(4, result.size());
    }

    /**
     * Проверяет поиск города по идентификатору.
     */
    @Test
    void Given_existingId_When_getById_Then_returnsCity() {
        // Arrange
        CityServiceImpl service = new CityServiceImpl();
        String id = "9cfa1706-2ab2-4ebb-b9f4-76b08b71e26e";

        // Act
        var result = service.getById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Москва", result.get().getName());
    }

    /**
     * Проверяет поиск города по имени.
     */
    @Test
    void Given_existingName_When_getByName_Then_returnsCity() {
        // Arrange
        CityServiceImpl service = new CityServiceImpl();

        // Act
        var result = service.getByName("Омск");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("bd7ab214-fb32-4591-be65-80bc67b91401", result.get().getId());
    }
}
