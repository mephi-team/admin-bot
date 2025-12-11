package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.EnrollmentScriptFile;

@Repository
public interface EnrollmentScriptFileRepository extends JpaRepository<EnrollmentScriptFile, Long> {
}

