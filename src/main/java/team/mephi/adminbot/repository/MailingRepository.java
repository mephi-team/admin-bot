package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Mailing;

import java.util.List;

@Repository
public interface MailingRepository extends JpaRepository<Mailing, Long> {
    List<Mailing> findAllByOrderByCreatedAtDesc();

    @Query("SELECT m FROM Mailing m JOIN fetch m.createdBy JOIN FETCH m.createdBy")
    List<Mailing> findAllWithUsers();

    @Query("SELECT m FROM Mailing m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Mailing> findMailingByName(String query);

    @Query("SELECT count(m) FROM Mailing m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Integer countByName(String query);
}

