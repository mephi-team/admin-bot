package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserQuestionDto {
    Long id;
    String question;
    LocalDateTime date;
    String user;
    String role;
    String direction;
    String answer;
}
