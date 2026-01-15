package team.mephi.adminbot.service;

import team.mephi.adminbot.dto.ChatListItem;
import team.mephi.adminbot.dto.SimpleDialog;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления сообщениями и диалогами.
 */
public interface MessageService {
    /**
     * Находит все элементы чата по идентификатору диалога.
     *
     * @param dialogId Идентификатор диалога.
     * @return Список элементов чата для указанного диалога.
     */
    List<ChatListItem> findAllByDialogId(Long dialogId);

    /**
     * Находит диалог по его идентификатору.
     *
     * @param dialogId Идентификатор диалога.
     * @return Опциональный диалог, если он существует.
     */
    Optional<SimpleDialog> findById(Long dialogId);

    /**
     * Подсчитывает количество сообщений в диалоге по его идентификатору.
     *
     * @param dialogId Идентификатор диалога.
     * @return Количество сообщений в указанном диалоге.
     */
    Integer countByDialogId(Long dialogId);

    /**
     * Отправляет сообщение в указанный диалог.
     *
     * @param dialogId    Идентификатор диалога.
     * @param messageText Текст сообщения.
     */
    void send(Long dialogId, String messageText);

    /**
     * Подсчитывает количество непрочитанных сообщений.
     *
     * @return Количество непрочитанных сообщений.
     */
    Integer unreadCount();
}
