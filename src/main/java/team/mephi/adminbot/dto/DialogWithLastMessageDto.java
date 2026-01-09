package team.mephi.adminbot.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
            this.lastMessageAt = ((Timestamp) lastMessageAtRaw).toInstant();
        } else if (lastMessageAtRaw instanceof LocalDateTime) {
            this.lastMessageAt = ((LocalDateTime) lastMessageAtRaw).toInstant(ZoneOffset.UTC);
        } else if (lastMessageAtRaw instanceof Instant) {
            this.lastMessageAt = (Instant) lastMessageAtRaw;
        } else {
            this.lastMessageAt = null;
        }
    }
}
