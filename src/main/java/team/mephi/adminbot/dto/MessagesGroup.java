package team.mephi.adminbot.dto;

import lombok.Data;
import team.mephi.adminbot.model.Message;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class MessagesGroup {
    private String dateLabel;
    private List<Message> messages;

    public MessagesGroup(String dateLabel, List<Message> messages) {
        this.dateLabel = dateLabel;
        this.messages = messages;
    }
}