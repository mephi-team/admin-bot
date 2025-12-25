package team.mephi.adminbot.vaadin.mailings.service;

import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.CRUDViewCallback;

public interface MailingViewCallback extends CRUDViewCallback<SimpleMailing> {
    void confirmCancel(Long id, Runnable onConfirm);
    void confirmRetry(Long id, Runnable onConfirm);
    void showNotificationForCancel(Long id);
    void showNotificationForRetry(Long id);
}
