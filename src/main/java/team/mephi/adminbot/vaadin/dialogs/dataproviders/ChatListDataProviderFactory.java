package team.mephi.adminbot.vaadin.dialogs.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.MessageService;

/**
 * Фабрика для создания экземпляров ChatListDataProvider.
 */
@SpringComponent
public class ChatListDataProviderFactory {

    private final MessageService messageService;

    public ChatListDataProviderFactory(MessageService messageService) {
        this.messageService = messageService;
    }

    public ChatListDataProvider createDataProvider() {
        return new ChatListDataProvider(messageService);
    }
}
