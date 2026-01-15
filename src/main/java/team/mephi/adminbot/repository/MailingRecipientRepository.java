package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.MailingRecipient;

/**
 * Репозиторий для работы с сущностью MailingRecipient.
 */
@Repository
@SuppressWarnings("unused")
public interface MailingRecipientRepository extends JpaRepository<MailingRecipient, Long> {
}

