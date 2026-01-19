package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Объект передачи данных для простого пользователя.
 */
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
    private Set<SimpleDirection> direction;
    private String city;
    private Boolean pdConsent;
    private List<SimplePd> pdConsentLog;
    private TutorDto tutor;
    private String blockReason;
    private String warningReason;
    private String messageForUser;
}
