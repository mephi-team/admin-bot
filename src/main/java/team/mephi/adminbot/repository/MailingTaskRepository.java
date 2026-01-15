package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.MailingTask;

/**
 * Репозиторий для работы с сущностью MailingTask.
 */
@Repository
@SuppressWarnings("unused")
public interface MailingTaskRepository extends JpaRepository<MailingTask, Long> {
}

