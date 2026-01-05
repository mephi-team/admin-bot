package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CohortDto {
    private String id;
    private String name;
    private Boolean current;
}
