package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Direction;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью Direction.
 */
@Repository
@SuppressWarnings("unused")
public interface DirectionRepository extends JpaRepository<Direction, Long> {
    /**
     * Находит направление по его названию.
     *
     * @param name Название направления.
     * @return Опциональное направление.
     */
    Optional<Direction> findByName(String name);
}
