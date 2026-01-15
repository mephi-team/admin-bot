package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.RegistrationLog;

@Repository
@SuppressWarnings("unused")
public interface RegistrationLogRepository extends JpaRepository<RegistrationLog, Long> {
}

