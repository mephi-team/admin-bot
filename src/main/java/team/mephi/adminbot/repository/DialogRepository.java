package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Dialog;

import java.time.Instant;
import java.util.List;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Long> {
    long countByLastMessageAtAfter(Instant dateTime);

    @Query("SELECT d FROM Dialog d WHERE d.lastMessageAt >= :oneWeekAgo")
    List<Dialog> findLastWeekDialogs(Instant oneWeekAgo);

    // Загружаем диалоги с пользователями (без сообщений — lazy loading)
    @Query("SELECT d FROM Dialog d JOIN FETCH d.user JOIN FETCH d.direction JOIN FETCH d.user.role ORDER BY d.lastMessageAt DESC")
    List<Dialog> findAllWithUsers();

    // Поиск по имени пользователя или содержимому сообщений (упрощённо — только по имени)
    @Query("SELECT d FROM Dialog d LEFT JOIN FETCH d.user u WHERE LOWER(COALESCE(u.fullName, '')) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Dialog> searchByUserName(String query);
}