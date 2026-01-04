package team.mephi.adminbot.vaadin.mailings.presenter;

import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.CRUDViewCallback;

public interface MailingViewCallback extends CRUDViewCallback<SimpleMailing> {
    void confirmCancel(SimpleMailing id, SerializableConsumer<SimpleMailing> onConfirm);
    void confirmRetry(SimpleMailing id, SerializableConsumer<SimpleMailing> onConfirm);
    void showNotificationForCancel(Long id);
    void showNotificationForRetry(Long id);
}
