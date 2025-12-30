package team.mephi.adminbot.vaadin.users.presenter;

import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDViewCallback;

public interface BlockingViewCallback extends CRUDViewCallback<SimpleUser> {
    void showDialogForBlock(SimpleUser user, SerializableConsumer<SimpleUser> callback);
    void showNotificationForBlock(Long id);
}
