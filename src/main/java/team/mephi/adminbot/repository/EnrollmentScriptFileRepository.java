package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.EnrollmentScriptFile;

/**
 * Репозиторий для работы с сущностью EnrollmentScriptFile.
 */
@Repository
@SuppressWarnings("unused")
public interface EnrollmentScriptFileRepository extends JpaRepository<EnrollmentScriptFile, Long> {
}

