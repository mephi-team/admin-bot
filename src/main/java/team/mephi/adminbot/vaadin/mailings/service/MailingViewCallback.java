package team.mephi.adminbot.vaadin.mailings.service;

import com.vaadin.flow.function.SerializableRunnable;

import java.util.List;

public interface MailingViewCallback<T> {
    void setOnSaveCallback(SerializableRunnable callback);
    T getEditedMailing();
    void showDialogForEdit(T user);
    void showDialogForNew(String role);
    void confirmDelete(List<Long> ids, Runnable onConfirm);
    void showNotification(String message);
}
