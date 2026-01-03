package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mephi.adminbot.model.Direction;

import java.util.Optional;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
    Optional<Direction> findByName(String name);
}
