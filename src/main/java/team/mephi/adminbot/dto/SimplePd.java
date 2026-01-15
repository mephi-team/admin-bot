package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Объект передачи данных для ПД.
 */
@Data
@Builder
public class SimplePd {
    private Long id;
    private String source;
    private String status;
}
