package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.dto.DialogWithLastMessageDto;
import team.mephi.adminbot.model.Dialog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Long> {
    long countByLastMessageAtAfter(LocalDateTime dateTime);

    @Query("SELECT d FROM Dialog d WHERE d.lastMessageAt >= :oneWeekAgo")
    List<Dialog> findLastWeekDialogs(LocalDateTime oneWeekAgo);

    // Загружаем диалоги с пользователями (без сообщений — lazy loading)
    @Query("SELECT d FROM Dialog d JOIN FETCH d.user JOIN FETCH d.direction JOIN FETCH d.user.role ORDER BY d.lastMessageAt DESC")
    List<Dialog> findAllWithUsers();

    // Поиск по имени пользователя или содержимому сообщений (упрощённо — только по имени)
    @Query("SELECT d FROM Dialog d LEFT JOIN FETCH d.user WHERE LOWER(d.user.userName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Dialog> searchByUserName(String query);

    @Query(value = """
    SELECT 
        d.id AS dialogId,
        u.last_name AS userLastName,
        u.first_name AS userFirstName,
        r.description AS userRoleDescription,
        u.tg_id AS userExternalId,
        d.last_message_at AS lastMessageAt,
        m.text AS lastMessageText,
        m.sender_type AS lastMessageSenderType,
        CASE
            WHEN m.sender_type = 'user' THEN
                CONCAT(s.last_name, ' ', SUBSTRING(s.first_name, 1, 1), '.')
            ELSE 'Администратор'
            END AS lastMessageSenderName
    FROM dialogs d
    JOIN users u ON d.user_id = u.id
    JOIN roles r ON u.role_code = r.code
    LEFT JOIN (
        SELECT DISTINCT ON (dialog_id) *
        FROM dialog_messages
        ORDER BY dialog_id, created_at DESC
    ) m ON m.dialog_id = d.id
    LEFT JOIN users s ON m.sender_id = s.id
    WHERE LOWER(u.user_name) LIKE LOWER(CONCAT('%', :query, '%'))
    ORDER BY d.last_message_at DESC
    """, nativeQuery = true)
    List<DialogWithLastMessageDto> findDialogsWithLastMessageNative(String query);

    @Query(value = """
    SELECT 
        count(1)
    FROM dialogs d
    JOIN users u ON d.user_id = u.id
    WHERE LOWER(u.user_name) LIKE LOWER(CONCAT('%', :query, '%'))
    """, nativeQuery = true)
    Integer countDialogsWithLastMessageNative(String query);
}