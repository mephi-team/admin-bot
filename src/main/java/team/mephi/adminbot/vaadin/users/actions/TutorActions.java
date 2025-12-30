package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDActions;

public interface TutorActions extends CRUDActions<SimpleUser>, BlockingActions {
    void onTutoring(Long id);
}
