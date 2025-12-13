package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MailingList {
    Long id;
    String name;
    LocalDateTime date;
    String users;
    String cohort;
    String direction;
    String curator;
    String city;
    String text;
    String status;
    String createdBy;
}
