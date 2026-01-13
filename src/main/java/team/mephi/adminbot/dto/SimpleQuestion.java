package team.mephi.adminbot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleQuestion {
    Long id;
    Instant date;
    Long authorId;
    String author;
    String role;
    String direction;
    String text;
    @NotEmpty
    String answer;
}
