package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.EnrollmentLink;

/**
 * Репозиторий для работы с сущностью EnrollmentBatch.
 */
@Repository
@SuppressWarnings("unused")
public interface EnrollmentLinkRepository extends JpaRepository<EnrollmentLink, Long> {
}

