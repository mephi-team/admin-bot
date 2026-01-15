package team.mephi.adminbot.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.model.enums.MailingStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Репозиторий для работы с сущностью Mailing.
 */
@Repository
@SuppressWarnings("unused")
public interface MailingRepository extends JpaRepository<Mailing, Long> {
    // Получение всех рассылок, отсортированных по дате создания в порядке убывания
    List<Mailing> findAllByOrderByCreatedAtDesc();

    // Получение всех рассылок вместе с информацией о пользователях, которые их создали
    @Query("SELECT m FROM Mailing m JOIN fetch m.createdBy JOIN FETCH m.createdBy")
    List<Mailing> findAllWithUsers();

    // Поиск рассылок по имени с учетом статусов и пагинацией
    @Query(value = "SELECT * FROM mailings m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) AND m.status IN :statuses", nativeQuery = true)
    List<Mailing> findMailingByName(String query, List<String> statuses, Pageable pageable);

    // Подсчет количества рассылок по имени с учетом статусов
    @Query("SELECT count(m) FROM Mailing m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) AND m.status IN :statuses")
    Integer countByName(String query, List<MailingStatus> statuses);

    // Подсчет количества рассылок по статусам и шаблонов
    @Query("SELECT 'SENT', count(m) FROM Mailing m WHERE m.status != team.mephi.adminbot.model.enums.MailingStatus.DRAFT UNION ALL SELECT 'DRAFT', count(m) FROM Mailing m WHERE m.status = team.mephi.adminbot.model.enums.MailingStatus.DRAFT UNION ALL SELECT 'TEMPLATES', count(t) FROM MailTemplate t")
    List<Tuple> countsByStatusTuples();

    // Преобразование результата подсчета в карту статусов и их количеств
    default Map<String, Long> countsByStatus() {
        return countsByStatusTuples().stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class),
                        tuple -> tuple.get(1, Long.class)
                ));
    }
}

