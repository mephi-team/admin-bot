package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleDirection;

import java.util.List;
import java.util.Optional;

public interface DirectionService {
    List<SimpleDirection> getAllDirections();
    List<SimpleDirection> getAllDirections(Pageable pageable, String query);
    Integer countAllDirections(String query);
    Optional<SimpleDirection> getById(Long id);
    Optional<SimpleDirection> getByName(String name);
}
