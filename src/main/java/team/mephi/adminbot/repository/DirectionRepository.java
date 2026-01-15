package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Direction;

import java.util.Optional;

@Repository
@SuppressWarnings("unused")
public interface DirectionRepository extends JpaRepository<Direction, Long> {
    Optional<Direction> findByName(String name);
}
