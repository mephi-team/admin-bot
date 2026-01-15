package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.icon.Icon;
import team.mephi.adminbot.vaadin.core.DialogWithTitle;
import team.mephi.adminbot.vaadin.components.dialogs.SimpleConfirmDialog;

/**
 * Фабрика для создания диалогов различных типов.
 */
public interface DialogFactory {
    /**
     * Получает диалог по указанному типу.
     *
     * @param type Тип диалога.
     * @return Экземпляр диалога с заголовком.
     */
    DialogWithTitle getDialog(DialogType type);

    /**
     * Получает диалог подтверждения по указанному типу и иконке.
     *
     * @param type Тип диалога.
     * @param icon Иконка для диалога.
     * @return Экземпляр диалога подтверждения.
     */
    SimpleConfirmDialog getConfirmDialog(DialogType type, Icon icon);
}
