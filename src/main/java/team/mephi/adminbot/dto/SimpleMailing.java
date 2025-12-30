package team.mephi.adminbot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMailing {
    private Long id;
//    @NotEmpty
    private String name;
    @NotEmpty
    private String text;
    private String status;
    private Long userId;
    @NotEmpty
    private Set<String> channels;
    @NotEmpty
    private String users;
    @NotEmpty
    private String cohort;
    @NotEmpty
    private String direction;
    @NotEmpty
    private String city;
    private Instant date;
    private String curator;
}
