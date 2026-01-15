package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Объект передачи данных для репетитора с количеством студентов и направлений.
 */
@Data
@AllArgsConstructor
@SuppressWarnings("unused")
public class TutorWithCounts {
    private Long id;
    private String fullName;
    private String tgId;
    private String email;
    private Boolean delete;
    private Long studentCount;
    private String directions;
}
