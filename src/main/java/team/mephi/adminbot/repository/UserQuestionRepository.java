package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.UserQuestion;

@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
}

