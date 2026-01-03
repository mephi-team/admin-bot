package team.mephi.adminbot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUser {
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
    private String direction;
    private String city;
    private Boolean pdConsent;
    private long studentCount;
    private String tutor;
}
