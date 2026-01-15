package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.UserAnswer;

@Repository
@SuppressWarnings("unused")
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    @Query("SELECT count(a) FROM  UserAnswer a WHERE a.question.id = :id")
    Integer countByQuestionId(Long id);
}

