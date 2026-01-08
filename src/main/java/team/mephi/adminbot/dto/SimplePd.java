package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimplePd {
    private Long id;
    private String source;
    private String status;
}
