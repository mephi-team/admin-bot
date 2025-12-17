package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleDto {
    private String code;
    private String name;
    private String description;
}
