package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.vaadin.service.DialogType;

/**
 * Интерфейс для действий, связанных с блокировкой элементов.
 *
 * @param <T> тип элемента, который будет блокироваться
 */
public interface BlockingActions<T> {
    /**
     * Обрабатывает блокировку элемента заданного типа диалога и дополнительных параметров.
     *
     * @param item   элемент, который будет блокироваться
     * @param type   тип диалога
     * @param params дополнительные параметры
     */
    void onBlock(T item, DialogType type, Object... params);
}
