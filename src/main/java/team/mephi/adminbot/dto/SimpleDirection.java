package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Объект передачи данных для простого направления.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDirection {
    private Long id;
    private String name;
}
