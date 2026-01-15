package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.EnrollmentScriptTask;

@Repository
@SuppressWarnings("unused")
public interface EnrollmentScriptTaskRepository extends JpaRepository<EnrollmentScriptTask, Long> {
}

