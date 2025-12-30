package team.mephi.adminbot.vaadin.users.presenter;

import com.vaadin.flow.function.SerializableRunnable;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDViewCallback;

public interface BlockingViewCallback extends CRUDViewCallback<SimpleUser> {
    void showDialogForBlock(SimpleUser user, SerializableRunnable callback);
    void showNotificationForBlock(Long id);
}
