package team.mephi.adminbot.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class DialogWithLastMessageDto {
    private Long dialogId;
    private String userLastName;
    private String userFirstName;
    private String userRoleDescription;
    private String userExternalId;
    private Object lastMessageAt;
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
            String lastMessageText,
            String lastMessageSenderType,
            String lastMessageSenderName
    ) {
        this.dialogId = dialogId;
        this.userLastName = userLastName;
        this.userFirstName = userFirstName;
        this.userRoleDescription = userRoleDescription;
        this.userExternalId = userExternalId;
        this.lastMessageText = lastMessageText;
        this.lastMessageSenderType = lastMessageSenderType;
        this.lastMessageSenderName = lastMessageSenderName;

        if (lastMessageAtRaw instanceof Timestamp) {
            this.lastMessageAt = ((Timestamp) lastMessageAtRaw).toLocalDateTime();
        } else if (lastMessageAtRaw instanceof LocalDateTime) {
            this.lastMessageAt = lastMessageAtRaw;
        } else {
            this.lastMessageAt = null;
        }
    }
}
