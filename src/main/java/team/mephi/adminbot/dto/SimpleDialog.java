package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleDialog {
    private Long id;
    private String userName;
    private String tgId;
    private String role;
    private String direction;
    private String cohort;
}
