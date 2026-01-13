package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TutorWithCounts {
    private Long id;
    private String fullName;
    private String tgId;
    private String email;
    private Boolean delete;
    private Long studentCount;
    private String directions;
}
