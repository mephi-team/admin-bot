package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.UtmReport;

@Repository
@SuppressWarnings("unused")
public interface UtmReportRepository extends JpaRepository<UtmReport, Long> {
}

