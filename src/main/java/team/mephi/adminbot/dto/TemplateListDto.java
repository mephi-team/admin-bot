package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Объект передачи данных для шаблона в списке.
 */
@Data
@Builder
@AllArgsConstructor
@SuppressWarnings("unused")
public class TemplateListDto {
    private Long id;
    private String name;
    private String text;
}
