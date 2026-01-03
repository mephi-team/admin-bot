package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.CohortDto;

import java.util.List;
import java.util.Optional;

public interface CohortService {
    List<CohortDto> getAllCohorts(Pageable pageable, String query);
    Optional<CohortDto> getById(String id);
    Optional<CohortDto> getByName(String name);
}
