package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.dto.CohortDto;

import static org.assertj.core.api.Assertions.assertThat;

class CohortServiceImplTest {
    private final CohortServiceImpl service = new CohortServiceImpl();

    @Test
    void getAllCohortsReturnsConfiguredList() {
        assertThat(service.getAllCohorts())
                .extracting(CohortDto::getName)
                .contains("Весна 2026", "Зима 2025", "Осень 2025", "Лето 2025", "Весна 2025");
    }

    @Test
    void getByIdFindsCohort() {
        assertThat(service.getById("4e62388e-6e82-4ea2-aa0f-5571661c7358"))
                .isPresent()
                .get()
                .extracting(CohortDto::getName)
                .isEqualTo("Весна 2026");
    }
}
