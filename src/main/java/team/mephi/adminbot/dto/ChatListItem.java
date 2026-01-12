package team.mephi.adminbot.dto;

import lombok.Getter;

public class ChatListItem {
    @Getter
    private final boolean isHeader;
    @Getter
    private final String dateLabel; // только для isHeader = true
    @Getter
    private final MessagesForListDto message; // только для isHeader = false

    private ChatListItem(String dateLabel) {
        this.isHeader = true;
        this.dateLabel = dateLabel;
        this.message = null;
    }

    private ChatListItem(MessagesForListDto message) {
        this.isHeader = false;
        this.dateLabel = null;
        this.message = message;
    }

    public static ChatListItem header(String label) {
        return new ChatListItem(label);
    }

    public static ChatListItem message(MessagesForListDto msg) {
        return new ChatListItem(msg);
    }
}