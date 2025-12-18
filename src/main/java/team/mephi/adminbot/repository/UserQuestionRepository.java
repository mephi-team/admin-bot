package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.UserQuestion;

import java.time.Instant;
import java.util.List;

@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    @Query("SELECT q FROM UserQuestion q JOIN FETCH q.user LEFT JOIN FETCH q.user.direction LEFT JOIN FETCH q.answers WHERE LOWER(q.text) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<UserQuestion> findAllByText(String query);

    @Query("SELECT count(q) FROM UserQuestion q WHERE LOWER(q.text) LIKE LOWER(CONCAT('%', :query, '%'))")
    Integer countByText(String query);

    @Query("SELECT q FROM UserQuestion q JOIN FETCH q.answers")
    List<UserQuestion> findAllWithAnswers();

    @Query("SELECT count(q) FROM UserQuestion q WHERE q.status = team.mephi.adminbot.model.enums.QuestionStatus.NEW")
    Integer countNewQuestion();

    long countByCreatedAtAfter(Instant createdAt);
}

