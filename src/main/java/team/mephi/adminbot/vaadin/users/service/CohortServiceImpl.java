package team.mephi.adminbot.vaadin.users.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.vaadin.users.components.CohortService;

import java.util.List;
import java.util.Optional;

@Service
public class CohortServiceImpl implements CohortService {
    @Override
    public List<CohortDto> getAllCohorts(Pageable pageable, String query) {
        return List.of(new CohortDto(1L, "Весна 2025"));
    }

    @Override
    public Optional<CohortDto> getById(Long id) {
        return Optional.empty();
    }
}
