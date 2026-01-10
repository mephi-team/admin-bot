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
public class SimpleUser {
    private Long id;
    private String role;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String tgId;
    private String status;
    private String fullName;
    private String tgName;
    private String cohort;
    private SimpleDirection direction;
    private String city;
    private Boolean pdConsent;
    private List<SimplePd> pdConsentLog;
    private SimpleTutor tutor;
}
