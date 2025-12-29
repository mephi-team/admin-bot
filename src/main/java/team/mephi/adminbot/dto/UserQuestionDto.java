package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserQuestionDto {
    Long id;
    String question;
    Instant date;
    Long userId;
    String user;
    String role;
    String direction;
    String answer;
}
