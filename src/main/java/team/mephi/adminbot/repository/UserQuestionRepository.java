package team.mephi.adminbot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.UserQuestion;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@SuppressWarnings("unused")
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    @Query("SELECT q FROM UserQuestion q JOIN FETCH q.user LEFT JOIN FETCH q.direction LEFT JOIN FETCH q.answers WHERE LOWER(q.text) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<UserQuestion> findAllByText(String query, Pageable pageable);

    @Query("SELECT count(q) FROM UserQuestion q WHERE LOWER(q.text) LIKE LOWER(CONCAT('%', :query, '%'))")
    Integer countByText(String query);

    @Query("SELECT q FROM UserQuestion q JOIN FETCH q.answers")
    List<UserQuestion> findAllWithAnswers();

    @Query("SELECT count(q) FROM UserQuestion q WHERE q.status = team.mephi.adminbot.model.enums.QuestionStatus.NEW")
    Integer countNewQuestion();

    long countByCreatedAtAfter(Instant createdAt);

    @Query("SELECT q FROM UserQuestion q JOIN FETCH q.user JOIN FETCH q.direction LEFT JOIN FETCH q.answers WHERE q.id = :id")
    Optional<UserQuestion> findByIdWithDeps(Long id);
}

