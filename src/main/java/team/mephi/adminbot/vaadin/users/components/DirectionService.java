package team.mephi.adminbot.vaadin.users.components;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.DirectionDto;

import java.util.List;
import java.util.Optional;

public interface DirectionService {
    List<DirectionDto> getAllDirections(Pageable pageable, String query);
    Optional<DirectionDto> getById(Long id);
}
