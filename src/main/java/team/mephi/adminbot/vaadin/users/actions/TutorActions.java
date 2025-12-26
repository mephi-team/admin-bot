package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.vaadin.CRUDActions;

import java.util.List;

public interface TutorActions extends CRUDActions {
    void onBlock(List<Long> ids);
    void onTutoring(Long id);
}
