package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String fullName;
    private String email;
    private String tgName;
    private String phoneNumber;
    private Boolean pdConsent;
    private String cohort;
    private String direction;
    private String city;
    private String status;
}
