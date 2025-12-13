package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.AdminAudit;

@Repository
public interface AdminAuditRepository extends JpaRepository<AdminAudit, Long> {
}

