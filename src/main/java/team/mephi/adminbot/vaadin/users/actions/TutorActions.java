package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.vaadin.CRUDActions;

public interface TutorActions extends CRUDActions, BlockingActions {
    void onTutoring(Long id);
}
