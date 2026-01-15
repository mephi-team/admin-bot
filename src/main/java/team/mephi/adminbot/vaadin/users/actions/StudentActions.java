package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.vaadin.service.DialogType;

import java.util.List;

/**
 * Интерфейс для действий, связанных со студентами.
 */
public interface StudentActions extends UserActions {
    /**
     * Обрабатывает отчисление студентов по заданным идентификаторам и типу диалога.
     *
     * @param ids    Список идентификаторов студентов.
     * @param type   Тип диалога.
     * @param params Дополнительные параметры.
     */
    void onExpel(List<Long> ids, DialogType type, Object... params);
}
