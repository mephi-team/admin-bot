package team.mephi.adminbot.dto;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String email;
    @NotEmpty
    private String phoneNumber;
    private String tgId;
    private String status;
    private String fullName;
    private String tgName;
    private String cohort;
    private SimpleDirection direction;
    private String city;
    private Boolean pdConsent;
    private long studentCount;
    private List<SimpleUser> students;
    private String tutor;
}
