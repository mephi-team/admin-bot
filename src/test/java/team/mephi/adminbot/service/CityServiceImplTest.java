package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.dto.CityDto;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для {@link CityServiceImpl}.
 */
class CityServiceImplTest {
    private final CityServiceImpl service = new CityServiceImpl();

    /**
     * Проверяет, что в списке городов есть ожидаемые значения.
     */
    @Test
    void givenService_WhenGetAllCitiesCalled_ThenListContainsDefaults() {
        // Arrange
        // Act
        var cities = service.getAllCities();

        // Assert
        assertThat(cities)
                .extracting(CityDto::getName)
                .contains("Все", "Москва", "Омск", "Санкт-Петербург");
    }

    /**
     * Проверяет поиск города по идентификатору.
     */
    @Test
    void givenExistingCityId_WhenGetByIdCalled_ThenCityReturned() {
        // Arrange
        String cityId = "bd7ab214-fb32-4591-be65-80bc67b91401";

        // Act
        var result = service.getById(cityId);

        // Assert
        assertThat(result)
                .isPresent()
                .get()
                .extracting(CityDto::getName)
                .isEqualTo("Омск");
    }
}
