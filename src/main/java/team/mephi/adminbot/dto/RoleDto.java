package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Объект передачи данных для роли пользователя.
 */
@Data
@Builder
@AllArgsConstructor
public class RoleDto {
    private String code;
    private String name;
}
