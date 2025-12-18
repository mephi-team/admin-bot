package team.mephi.adminbot.vaadin.mailings.service;

import team.mephi.adminbot.dto.SimpleUser;

import java.util.List;

public interface MailingViewCallback {
    void showUserEditorForView(SimpleUser user);
    void showUserEditorForEdit(SimpleUser user);
    void showUserEditorForNew(String role);
    void confirmDelete(List<Long> ids, Runnable onConfirm);
    void confirmAccept(List<Long> ids, Runnable onConfirm);
    void confirmReject(List<Long> ids, Runnable onConfirm);
    void showNotification(String message);
}
