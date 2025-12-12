package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;
import team.mephi.adminbot.model.User;

@Data
@Builder
public class MailingList {
    Long id;
    String name;
    String createdBy;
}
