package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDViewCallback;

public interface BlockingViewCallback extends CRUDViewCallback<SimpleUser> {
    void showDialogForBlock(SimpleUser user);
    void showNotificationForBlock(Long id);
}
