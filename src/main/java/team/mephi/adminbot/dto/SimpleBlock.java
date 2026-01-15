package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Объект передачи данных для простого бана пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class SimpleBlock {
    private Long id;
    private String firstName;
    private String lastName;
    private String tgId;
    private String blockReason;
    private String warningReason;
    private String message;
}
