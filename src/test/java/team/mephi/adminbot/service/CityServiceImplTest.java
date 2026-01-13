package team.mephi.adminbot.service;

import org.junit.jupiter.api.Test;
import team.mephi.adminbot.dto.CityDto;

import static org.assertj.core.api.Assertions.assertThat;

class CityServiceImplTest {
    private final CityServiceImpl service = new CityServiceImpl();

    @Test
    void getAllCitiesIncludesDefault() {
        assertThat(service.getAllCities())
                .extracting(CityDto::getName)
                .contains("Все", "Москва", "Омск", "Санкт-Петербург");
    }

    @Test
    void getByIdFindsCity() {
        assertThat(service.getById("bd7ab214-fb32-4591-be65-80bc67b91401"))
                .isPresent()
                .get()
                .extracting(CityDto::getName)
                .isEqualTo("Омск");
    }
}
