package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Объект передачи данных для репетитора с количеством студентов и направлений.
 */
@Data
@Builder
@AllArgsConstructor
@SuppressWarnings("unused")
public class TutorDto {
    private Long id;
    private String fullName;
}
