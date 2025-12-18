package team.mephi.adminbot.vaadin.mailings.service;

import com.vaadin.flow.function.SerializableRunnable;

import java.util.List;

public interface MailingViewCallback<T> {
    void setOnSaveCallback(SerializableRunnable callback);
    T getEditedMailing();
    void showUserEditorForView(T user);
    void showUserEditorForEdit(T user);
    void showUserEditorForNew(String role);
    void confirmDelete(List<Long> ids, Runnable onConfirm);
    void confirmAccept(List<Long> ids, Runnable onConfirm);
    void confirmReject(List<Long> ids, Runnable onConfirm);
    void showNotification(String message);
}
