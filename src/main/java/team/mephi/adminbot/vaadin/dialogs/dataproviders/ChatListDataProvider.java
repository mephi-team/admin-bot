package team.mephi.adminbot.vaadin.dialogs.dataproviders;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import team.mephi.adminbot.dto.ChatListItem;
import team.mephi.adminbot.dto.SimpleDialog;
import team.mephi.adminbot.service.MessageService;

import java.util.Optional;

/**
 * Провайдер данных для списка чата.
 * Предоставляет методы для получения и сохранения сообщений в диалогах.
 */
public class ChatListDataProvider {
    private final MessageService messageService;
    private ConfigurableFilterDataProvider<ChatListItem, Void, Long> provider;

    /**
     * Конструктор класса ChatListDataProvider.
     *
     * @param messageService сервис для работы с сообщениями
     */
    public ChatListDataProvider(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Возвращает провайдер данных с возможностью фильтрации по идентификатору диалога.
     *
     * @return провайдер данных с фильтром
     */
    public ConfigurableFilterDataProvider<ChatListItem, Void, Long> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<ChatListItem, Long>(
                    query -> {
                        var result = messageService.findAllByDialogId(query.getFilter().orElse(0L));
                        return result.stream()
                                .skip(query.getOffset())
                                .limit(query.getLimit());
                    },
                    query -> messageService.countByDialogId(query.getFilter().orElse(0L))
            ).withConfigurableFilter();
        }

        return provider;
    }

    /**
     * Находит диалог по его идентификатору.
     *
     * @param id идентификатор диалога
     * @return опциональный объект SimpleDialog, если диалог найден
     */
    public Optional<SimpleDialog> findById(Long id) {
        return messageService.findById(id);
    }

    /**
     * Сохраняет сообщение в указанном диалоге.
     *
     * @param dialogId    идентификатор диалога
     * @param messageText текст сообщения
     */
    public void save(Long dialogId, String messageText) {
        messageService.send(dialogId, messageText);
    }
}
