package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.service.DialogType;

/**
 * Интерфейс для действий, связанных с репетиторами.
 */
public interface TutorActions extends CRUDActions<SimpleTutor>, BlockingActions<SimpleTutor> {
    void onTutoring(SimpleTutor item, DialogType type, Object... params);
}
