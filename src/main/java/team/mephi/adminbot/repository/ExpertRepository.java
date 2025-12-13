package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Expert;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {
}

