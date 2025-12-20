package team.mephi.adminbot.vaadin.users.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.DirectionDto;
import team.mephi.adminbot.vaadin.users.components.DirectionService;

import java.util.List;
import java.util.Optional;

@Service
public class DirectionServiceImpl implements DirectionService {
    @Override
    public List<DirectionDto> getAllDirections(Pageable pageable, String query) {
        return List.of(new DirectionDto(1L, "Java"));
    }

    @Override
    public Optional<DirectionDto> getById(Long id) {
        return Optional.empty();
    }
}
