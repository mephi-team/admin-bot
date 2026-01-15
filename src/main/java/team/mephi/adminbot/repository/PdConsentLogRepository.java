package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.PdConsentLog;

/**
 * Репозиторий для управления сущностями PdConsentLog.
 */
@Repository
@SuppressWarnings("unused")
public interface PdConsentLogRepository extends JpaRepository<PdConsentLog, Long> {
}

