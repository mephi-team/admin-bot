package team.mephi.adminbot.vaadin.users.service;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDViewCallback;

public interface TutorViewCallback extends CRUDViewCallback<SimpleUser> {
    void showDialogForTutoring(SimpleUser user);
    void showNotificationForTutoring(Long id);
}
