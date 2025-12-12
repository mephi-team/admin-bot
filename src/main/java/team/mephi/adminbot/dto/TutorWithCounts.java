package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TutorWithCounts {
    private Long id;
    private String firstName;
    private String lastName;
    private String tgId;
    private String email;
    private Long studentCount;
    private String directions;
}
