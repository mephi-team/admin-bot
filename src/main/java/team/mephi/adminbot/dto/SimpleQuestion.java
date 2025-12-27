package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleQuestion {
    Long id;
    String author;
    String role;
    String direction;
    String text;
}
