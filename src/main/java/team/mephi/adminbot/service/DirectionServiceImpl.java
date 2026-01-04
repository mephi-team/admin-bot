package team.mephi.adminbot.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.repository.DirectionRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DirectionServiceImpl implements DirectionService {

    private final DirectionRepository directionRepository;
    private List<SimpleDirection> directions;

    public DirectionServiceImpl(DirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    @Override
    public List<SimpleDirection> getAllDirections(Pageable pageable, String query) {
        if (Objects.isNull(directions)) initDirections();
        return directions;
    }

    @Override
    public Optional<SimpleDirection> getById(Long id) {
        if (Objects.isNull(id)) return Optional.empty();
        return directionRepository.findById(id)
                .map(d -> SimpleDirection.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .build());
    }

    @Override
    public Optional<SimpleDirection> getByName(String name) {
        if (Objects.isNull(directions)) initDirections();
        return directions.stream().filter(d -> d.getName().equals(name)).findAny();
    }

    private void initDirections() {
        directions = directionRepository.findAll()
                .stream()
                .map(d -> SimpleDirection.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .build())
                .toList();
    }
}
