package team.mephi.adminbot.vaadin.mailings.actions;

import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.core.CRUDActions;
import team.mephi.adminbot.vaadin.service.DialogType;

/**
 * Интерфейс действий для управления рассылками.
 */
public interface MailingActions extends CRUDActions<SimpleMailing> {
    /**
     * Обрабатывает отмену рассылки.
     *
     * @param ids    идентификаторы рассылок
     * @param type   тип диалога
     * @param params дополнительные параметры
     */
    void onCancel(SimpleMailing ids, DialogType type, Object... params);

    /**
     * Обрабатывает повторную отправку рассылки.
     *
     * @param ids    идентификаторы рассылок
     * @param type   тип диалога
     * @param params дополнительные параметры
     */
    void onRetry(SimpleMailing ids, DialogType type, Object... params);
}
