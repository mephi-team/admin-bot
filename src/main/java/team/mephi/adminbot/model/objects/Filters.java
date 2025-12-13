package team.mephi.adminbot.model.objects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Filters {
    private String users;
    private String cohort;
    private String direction;
    private String city;
    private String curator;
}
