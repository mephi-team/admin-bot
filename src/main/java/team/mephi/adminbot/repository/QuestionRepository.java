package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Question;

import java.time.Instant;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    long countByCreatedAtAfter(Instant createdAt);

    @Query("SELECT q FROM Question q WHERE LOWER(q.questionText) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Question> findAllLikeQuestionText(String query);

    @Query("SELECT count(q) FROM Question q WHERE LOWER(q.questionText) LIKE LOWER(CONCAT('%', :query, '%'))")
    Integer countByQuestionText(String query);
}
