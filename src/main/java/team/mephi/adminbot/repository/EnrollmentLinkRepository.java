package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.EnrollmentLink;

@Repository
public interface EnrollmentLinkRepository extends JpaRepository<EnrollmentLink, Long> {
}

