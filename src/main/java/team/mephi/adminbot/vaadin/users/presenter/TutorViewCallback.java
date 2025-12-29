package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;

public interface TutorViewCallback extends BlockingViewCallback {
    void showDialogForTutoring(SimpleUser user);
    void showNotificationForTutoring(Long id);
}
