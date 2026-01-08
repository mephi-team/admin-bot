package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.vaadin.CRUDActions;

public interface TutorActions extends CRUDActions<SimpleTutor>, BlockingActions<SimpleTutor> {
    void onTutoring(SimpleTutor item, String label, Object ... params);
}
