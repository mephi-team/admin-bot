package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMailing {
    private Long id;
    private String name;
    private String text;
    private Long userId;
}
