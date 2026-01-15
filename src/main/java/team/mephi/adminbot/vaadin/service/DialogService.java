package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.function.SerializableConsumer;

/**
 * Сервис для отображения диалогов различных типов.
 */
public interface DialogService<T> {
    /**
     * Отображает диалог указанного типа для пользователя с заданным обратным вызовом.
     *
     * @param user     Пользователь, для которого отображается диалог.
     * @param type     Тип диалога.
     * @param callback Обратный вызов для обработки результата диалога.
     */
    void showDialog(Object user, DialogType type, SerializableConsumer<T> callback);

    /**
     * Отображает диалог подтверждения указанного типа с иконкой для пользователя с заданным обратным вызовом.
     *
     * @param user     Пользователь, для которого отображается диалог.
     * @param type     Тип диалога.
     * @param icon     Иконка для диалога.
     * @param callback Обратный вызов для обработки результата диалога.
     */
    void showConfirmDialog(Object user, DialogType type, Icon icon, SerializableConsumer<T> callback);
}
