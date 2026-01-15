package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

/**
 * Объект передачи данных для сообщения в списке.
 */
@Data
@AllArgsConstructor
public class MessagesForListDto {
    private Long id;
    private String text;
    private Instant date;
    private String senderType;
}
