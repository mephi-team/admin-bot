package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Message;

import java.util.List;

/**
 * Репозиторий для управления сущностями Message.
 */
@Repository
@SuppressWarnings("unused")
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Находит все сообщения по идентификатору диалога.
     *
     * @param dialogId Идентификатор диалога.
     * @return Список сообщений для указанного диалога.
     */
    List<Message> findAllByDialogId(Long dialogId);

    /**
     * Подсчитывает количество сообщений для заданного диалога.
     *
     * @param dialogId Идентификатор диалога.
     * @return Количество сообщений для указанного диалога.
     */
    Integer countByDialogId(Long dialogId);

    /**
     * Подсчитывает количество уникальных дней, в которые были созданы сообщения в заданном диалоге.
     *
     * @param dialogId Идентификатор диалога.
     * @return Количество уникальных дней с сообщениями для указанного диалога.
     */
    @Query(value = "SELECT COUNT(DISTINCT CAST(created_at AT TIME ZONE 'UTC' AS DATE)) FROM dialog_messages WHERE dialog_id = :dialogId", nativeQuery = true)
    Integer countByDialogIdAndCreatedAt(Long dialogId);
}
