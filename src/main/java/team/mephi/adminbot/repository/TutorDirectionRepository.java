package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.TutorDirection;

@Repository
public interface TutorDirectionRepository extends JpaRepository<TutorDirection, TutorDirection.TutorDirectionId> {
}

