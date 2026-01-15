package team.mephi.adminbot.vaadin.dialogs.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.MessageService;

/**
 * Фабрика для создания экземпляров ChatListDataProvider.
 */
@SpringComponent
public class ChatListDataProviderFactory {

    private final MessageService messageService;

    /**
     * Конструктор фабрики с внедрением зависимости MessageService.
     *
     * @param messageService сервис для работы с сообщениями
     */
    public ChatListDataProviderFactory(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Создает новый экземпляр ChatListDataProvider.
     *
     * @return новый экземпляр ChatListDataProvider
     */
    public ChatListDataProvider createDataProvider() {
        return new ChatListDataProvider(messageService);
    }
}
