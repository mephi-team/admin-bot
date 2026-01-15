package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Объект передачи данных для вопроса пользователя.
 */
@Data
@Builder
@SuppressWarnings("unused")
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
