package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.EnrollmentBatch;

@Repository
public interface EnrollmentBatchRepository extends JpaRepository<EnrollmentBatch, Long> {
}

