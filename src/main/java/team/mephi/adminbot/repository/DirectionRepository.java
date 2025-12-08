package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mephi.adminbot.model.Direction;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
}
