package team.mephi.adminbot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleTemplate {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String text;
}
