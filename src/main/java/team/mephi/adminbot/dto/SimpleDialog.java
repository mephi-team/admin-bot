package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Объект передачи данных для простого диалога.
 */
@Data
@Builder
public class SimpleDialog {
    private Long id;
    private String userName;
    private String tgId;
    private String role;
    private String direction;
    private String cohort;
}
