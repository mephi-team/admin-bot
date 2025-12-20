package team.mephi.adminbot.vaadin.users.components;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.CohortDto;

import java.util.List;
import java.util.Optional;

public interface CohortService {
    List<CohortDto> getAllCohorts(Pageable pageable, String query);
    Optional<CohortDto> getById(Long id);
}
