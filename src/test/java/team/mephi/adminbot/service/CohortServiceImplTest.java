package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.dto.CohortDto;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для {@link CohortServiceImpl}.
 */
class CohortServiceImplTest {
    private final CohortServiceImpl service = new CohortServiceImpl();

    /**
     * Проверяет, что список когорт содержит ожидаемые значения.
     */
    @Test
    void givenService_WhenGetAllCohortsCalled_ThenListContainsDefaults() {
        // Arrange
        // Act
        var cohorts = service.getAllCohorts();

        // Assert
        assertThat(cohorts)
                .extracting(CohortDto::getName)
                .contains("Весна 2026", "Зима 2025", "Осень 2025", "Лето 2025", "Весна 2025");
    }

    /**
     * Проверяет поиск когорты по идентификатору.
     */
    @Test
    void givenExistingCohortId_WhenGetByIdCalled_ThenCohortReturned() {
        // Arrange
        String cohortId = "4e62388e-6e82-4ea2-aa0f-5571661c7358";

        // Act
        var result = service.getById(cohortId);

        // Assert
        assertThat(result)
                .isPresent()
                .get()
                .extracting(CohortDto::getName)
                .isEqualTo("Весна 2026");
    }
}
