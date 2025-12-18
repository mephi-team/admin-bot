package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.model.enums.MailingStatus;

import java.util.List;

@Repository
public interface MailingRepository extends JpaRepository<Mailing, Long> {
    List<Mailing> findAllByOrderByCreatedAtDesc();

    @Query("SELECT m FROM Mailing m JOIN fetch m.createdBy JOIN FETCH m.createdBy")
    List<Mailing> findAllWithUsers();

    @Query("SELECT m FROM Mailing m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) AND m.status IN :statuses")
    List<Mailing> findMailingByName(String query, List<MailingStatus> statuses);

    @Query("SELECT count(m) FROM Mailing m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) AND m.status IN :statuses")
    Integer countByName(String query, List<MailingStatus> statuses);
}

