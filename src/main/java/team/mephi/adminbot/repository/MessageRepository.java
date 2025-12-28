package team.mephi.adminbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.mephi.adminbot.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByDialogId(Long dialogId);

    Integer countByDialogId(Long dialogId);

    @Query(value = "SELECT COUNT(DISTINCT CAST(created_at AS DATE)) FROM dialog_messages WHERE dialog_id = :dialogId", nativeQuery = true)
    Integer countByDialogIdAndCreatedAt(Long dialogId);
}
