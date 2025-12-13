package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.PdConsentLog;
import team.mephi.adminbot.model.User;

import java.util.List;

@Repository
public interface PdConsentLogRepository extends JpaRepository<PdConsentLog, Long> {

    @Query("""
        select l from PdConsentLog l
        where l.user.id = :userId
        order by l.consentedAt desc        
        """
    )
    List<PdConsentLog> findLatestByUserId(Long userId);

    Long user(User user);
}

