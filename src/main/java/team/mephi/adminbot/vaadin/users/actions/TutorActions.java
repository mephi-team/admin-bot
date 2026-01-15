package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.vaadin.core.CRUDActions;
import team.mephi.adminbot.vaadin.service.DialogType;

/**
 * Интерфейс для действий, связанных с репетиторами.
 */
public interface TutorActions extends CRUDActions<SimpleTutor>, BlockingActions<SimpleTutor> {
    /**
     * Обрабатывает действия, связанные с репетиторством для заданного репетитора и типа диалога.
     *
     * @param item   Репетитор.
     * @param type   Тип диалога.
     * @param params Дополнительные параметры.
     */
    void onTutoring(SimpleTutor item, DialogType type, Object... params);
}
