package team.mephi.adminbot.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Объект передачи данных для диалога с информацией о последнем сообщении.
 */
@Data
public class DialogWithLastMessageDto {
    private Long dialogId;
    private String userLastName;
    private String userFirstName;
    private String userRoleDescription;
    private String userExternalId;
    private Instant lastMessageAt;
    private Integer unreadCount;
    private String lastMessageText;
    private String lastMessageSenderType;
    private String lastMessageSenderName; // например, "Иванов А."

    /**
     * Конструктор для создания объекта передачи данных диалога с информацией о последнем сообщении.
     *
     * @param dialogId              Идентификатор диалога.
     * @param userLastName          Фамилия пользователя.
     * @param userFirstName         Имя пользователя.
     * @param userRoleDescription   Описание роли пользователя.
     * @param userExternalId        Внешний идентификатор пользователя.
     * @param lastMessageAtRaw      Время последнего сообщения (может быть разных типов).
     * @param unreadCount           Количество непрочитанных сообщений.
     * @param lastMessageText       Текст последнего сообщения.
     * @param lastMessageSenderType Тип отправителя последнего сообщения.
     * @param lastMessageSenderName Имя отправителя последнего сообщения.
     */
    public DialogWithLastMessageDto(
            Long dialogId,
            String userLastName,
            String userFirstName,
            String userRoleDescription,
            String userExternalId,
            Object lastMessageAtRaw, // примет Timestamp
            Integer unreadCount,
            String lastMessageText,
            String lastMessageSenderType,
            String lastMessageSenderName
    ) {
        this.dialogId = dialogId;
        this.userLastName = userLastName;
        this.userFirstName = userFirstName;
        this.userRoleDescription = userRoleDescription;
        this.userExternalId = userExternalId;
        this.unreadCount = unreadCount;
        this.lastMessageText = lastMessageText;
        this.lastMessageSenderType = lastMessageSenderType;
        this.lastMessageSenderName = lastMessageSenderName;

        switch (lastMessageAtRaw) {
            case Timestamp timestamp -> this.lastMessageAt = timestamp.toInstant();
            case LocalDateTime localDateTime -> this.lastMessageAt = localDateTime.toInstant(ZoneOffset.UTC);
            case Instant instant -> this.lastMessageAt = instant;
            case null, default -> this.lastMessageAt = null;
        }
    }
}
