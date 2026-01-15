package team.mephi.adminbot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.MailTemplate;

import java.util.List;

/**
 * Репозиторий для работы с сущностью MailTemplate.
 */
@Repository
@SuppressWarnings("unused")
public interface MailTemplateRepository extends JpaRepository<MailTemplate, Long> {
    @Query("SELECT t FROM MailTemplate t WHERE LOWER(COALESCE(t.name, '')) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<MailTemplate> findAllByName(String query, Pageable pageable);

    @Query("SELECT count(t) FROM MailTemplate t WHERE LOWER(COALESCE(t.name, '')) LIKE LOWER(CONCAT('%', :query, '%'))")
    Integer countByName(String query);
}

