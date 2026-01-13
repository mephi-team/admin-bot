package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleTutor {
    private Long id;
    private String role;
    private String firstName;
    private String lastName;
    private String competenceCenter;
    private String email;
    private String phoneNumber;
    private String tgId;
    private String status;
    private String fullName;
    private String tgName;
    private String cohort;
    private List<SimpleDirection> directions;
    private String city;
    private long studentCount;
    private List<SimpleUser> students;
}
