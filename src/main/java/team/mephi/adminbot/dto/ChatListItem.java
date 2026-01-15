package team.mephi.adminbot.dto;

import lombok.Getter;

/**
 * Элемент списка чата, который может быть либо заголовком (дата), либо сообщением.
 */
public class ChatListItem {
    @Getter
    private final boolean isHeader;
    @Getter
    private final String dateLabel; // только для isHeader = true
    @Getter
    private final MessagesForListDto message; // только для isHeader = false

    /* Приватный конструктор для создания заголовка */
    private ChatListItem(String dateLabel) {
        this.isHeader = true;
        this.dateLabel = dateLabel;
        this.message = null;
    }

    /* Приватный конструктор для создания элемента сообщения */
    private ChatListItem(MessagesForListDto message) {
        this.isHeader = false;
        this.dateLabel = null;
        this.message = message;
    }

    /* Статический фабричный метод для создания заголовка */
    public static ChatListItem header(String label) {
        return new ChatListItem(label);
    }

    /* Статический фабричный метод для создания элемента сообщения */
    public static ChatListItem message(MessagesForListDto msg) {
        return new ChatListItem(msg);
    }
}