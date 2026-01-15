package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.vaadin.service.DialogType;

import java.util.List;

/**
 * Интерфейс для действий, которые могут быть приняты или отклонены.
 */
public interface AcceptableActions {
    /**
     * Обрабатывает принятие действий для заданных идентификаторов и типа диалога.
     *
     * @param ids    Список идентификаторов действий.
     * @param type   Тип диалога.
     * @param params Дополнительные параметры.
     */
    void onAccept(List<Long> ids, DialogType type, Object... params);

    /**
     * Обрабатывает отклонение действий для заданных идентификаторов и типа диалога.
     *
     * @param ids    Список идентификаторов действий.
     * @param type   Тип диалога.
     * @param params Дополнительные параметры.
     */
    void onReject(List<Long> ids, DialogType type, Object... params);
}
