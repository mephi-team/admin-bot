package team.mephi.adminbot.dto;

import lombok.Data;

@Data
public class CreateUserFormDto {
    private String roleName;
    private String fullName;
    private String telegram;
}
