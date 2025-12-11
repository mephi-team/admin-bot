package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.MailingRecipient;

@Repository
public interface MailingRecipientRepository extends JpaRepository<MailingRecipient, Long> {
}

