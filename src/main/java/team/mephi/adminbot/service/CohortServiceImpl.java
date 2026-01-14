package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.CohortDto;

import java.util.List;
import java.util.Optional;

@Service
public class CohortServiceImpl implements CohortService {

    private final List<CohortDto> cohorts = List.of(
            new CohortDto("4e62388e-6e82-4ea2-aa0f-5571661c7358", "Весна 2026", true),
            new CohortDto("d7f480c7-5e1d-4652-91de-82b9bde324d4", "Зима 2025", false),
            new CohortDto("bd7ab214-fb32-4591-be65-80bc67b91401", "Осень 2025", false),
            new CohortDto("47822b46-a33b-4f9a-aee0-50937f6baff4", "Лето 2025", false),
            new CohortDto("9cfa1706-2ab2-4ebb-b9f4-76b08b71e26e", "Весна 2025", false)
    );

    @Override
    public List<CohortDto> getAllCohorts() {
        return cohorts;
    }

    @Override
    public List<CohortDto> getAllCohorts(Pageable pageable, String query) {
        return cohorts;
    }

    @Override
    public Optional<CohortDto> getById(String id) {
        return cohorts.stream().filter(c -> c.getId().equals(id)).findAny();
    }

    @Override
    public Optional<CohortDto> getByName(String name) {
        return cohorts.stream().filter(c -> c.getName().equals(name)).findAny();
    }

    @Override
    public CohortDto getDefaultCohort() {
        return cohorts.stream().filter(CohortDto::getCurrent).findAny().orElse(cohorts.getFirst());
    }
}
