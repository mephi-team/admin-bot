package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
}
