package team.mephi.adminbot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMailing {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String text;
    private Long userId;
    private Set<String> channels;
}
