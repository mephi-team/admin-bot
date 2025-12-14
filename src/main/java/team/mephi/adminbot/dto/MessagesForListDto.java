package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessagesForListDto {
    private Long id;
    private String text;
    private String date;
    private String senderType;
}
