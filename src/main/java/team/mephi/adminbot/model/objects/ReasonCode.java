package team.mephi.adminbot.model.objects;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReasonCode {
    private List<String> users;
}
