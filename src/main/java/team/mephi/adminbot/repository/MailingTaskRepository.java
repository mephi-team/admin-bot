package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.MailingTask;

@Repository
public interface MailingTaskRepository extends JpaRepository<MailingTask, Long> {
}

