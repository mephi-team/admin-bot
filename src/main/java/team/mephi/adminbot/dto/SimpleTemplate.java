package team.mephi.adminbot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Объект передачи данных для простого шаблона.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleTemplate {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String text;
    private Instant date;
}
