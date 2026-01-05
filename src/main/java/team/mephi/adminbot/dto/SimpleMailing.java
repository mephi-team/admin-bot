package team.mephi.adminbot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMailing {
    private Long id;
//    @NotEmpty
    private String name;
    @NotEmpty
    private String text;
    private String status;
    @NotEmpty
    private Set<String> channels = Set.of("Email", "Telegram");
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
    private List<String> recipients = List.of();
}
