package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String telegram;
}
