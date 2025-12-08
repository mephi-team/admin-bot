package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Question;

import java.time.LocalDateTime;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    long countByCreatedAtAfter(LocalDateTime dateTime);
}
