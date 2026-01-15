package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Объект передачи данных для города.
 */
@Data
@AllArgsConstructor
public class CityDto {
    private String id;
    private String name;
}
