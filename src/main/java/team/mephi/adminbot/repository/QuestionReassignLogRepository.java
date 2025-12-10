package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.QuestionReassignLog;

@Repository
public interface QuestionReassignLogRepository extends JpaRepository<QuestionReassignLog, Long> {
}

