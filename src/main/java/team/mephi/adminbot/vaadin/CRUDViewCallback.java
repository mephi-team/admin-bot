package team.mephi.adminbot.vaadin;

import com.vaadin.flow.function.SerializableRunnable;

import java.util.List;

public interface CRUDViewCallback<T> {
    void setOnSaveCallback(SerializableRunnable callback);
    T getEditedItem();
    void showDialogForView(T user);
    void showDialogForNew(String role);
    void showDialogForEdit(T user);
    void confirmDelete(List<Long> ids, Runnable onConfirm);
    void showNotificationForNew();
    void showNotificationForEdit(Long id);
    void showNotificationForDelete(List<Long> ids);
}
