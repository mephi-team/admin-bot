package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.ExpertDirection;

@Repository
@SuppressWarnings("unused")
public interface ExpertDirectionRepository extends JpaRepository<ExpertDirection, ExpertDirection.ExpertDirectionId> {
}

