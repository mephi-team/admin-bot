package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.vaadin.service.DialogType;

/**
 * Интерфейс для действий, связанных с блокировкой элементов.
 *
 * @param <T> тип элемента, который будет блокироваться
 */
public interface BlockingActions<T> {
    void onBlock(T item, DialogType type, Object... params);
}
