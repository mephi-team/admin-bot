package team.mephi.adminbot.vaadin;

import com.vaadin.flow.function.SerializableRunnable;

import java.util.List;

public interface CRUDViewCallback<T> {
    Object getEditedItem();
    void showDialogForView(T user);
    void showDialogForNew(String role, SerializableRunnable callback);
    void showDialogForEdit(Object user, SerializableRunnable callback);
    void confirmDelete(List<Long> ids, Runnable onConfirm);
    void showNotificationForNew();
    void showNotificationForEdit(Long id);
    void showNotificationForDelete(List<Long> ids);
}
