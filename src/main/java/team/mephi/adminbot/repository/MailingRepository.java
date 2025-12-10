package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Mailing;

@Repository
public interface MailingRepository extends JpaRepository<Mailing, Long> {
}

