package team.mephi.adminbot.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class DialogWithLastMessageDto {
    private Long dialogId;
    private String userLastName;
    private String userFirstName;
    private String userRoleDescription;
    private String userExternalId;
    private LocalDateTime lastMessageAt;
    private Integer unreadCount;
    private String lastMessageText;
    private String lastMessageSenderType;
    private String lastMessageSenderName; // например, "Иванов А."

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

        if (lastMessageAtRaw instanceof Timestamp) {
            this.lastMessageAt = ((Timestamp) lastMessageAtRaw).toLocalDateTime();
        } else if (lastMessageAtRaw instanceof LocalDateTime) {
            this.lastMessageAt = (LocalDateTime) lastMessageAtRaw;
        } else if (lastMessageAtRaw instanceof Instant) {
            this.lastMessageAt = LocalDateTime.ofInstant((Instant) lastMessageAtRaw, ZoneId.of("UTC"));
        } else {
            this.lastMessageAt = null;
        }
    }
}
