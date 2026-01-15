package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@SuppressWarnings("unused")
public class MailingList {
    Long id;
    String name;
    Instant date;
    String users;
    String cohort;
    String direction;
    String curator;
    String city;
    String text;
    String status;
    String createdBy;
}
