package team.mephi.adminbot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleQuestion {
    Long id;
    String author;
    String role;
    String direction;
    String text;
    @NotEmpty
    String answer;
}
