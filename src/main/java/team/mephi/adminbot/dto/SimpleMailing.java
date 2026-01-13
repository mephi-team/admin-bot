package team.mephi.adminbot.dto;

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
    private String name;
    private String text;
    private String status;
    private Set<String> channels;
    private String users;
    private String cohort;
    private String direction;
    private String city;
    private Instant date;
    private String curator;
    private List<String> recipients;
}
