package team.mephi.adminbot.vaadin;

import com.vaadin.flow.function.SerializableConsumer;

import java.util.List;

public interface CRUDViewCallback<T> {
    void showDialogForView(T user);
    void showDialogForNew(String role, SerializableConsumer<?> callback);
    void showDialogForEdit(Object user, SerializableConsumer<?> callback);
    void confirmDelete(List<Long> ids, Runnable onConfirm);
    void showNotificationForNew();
    void showNotificationForEdit(Long id);
    void showNotificationForDelete(List<Long> ids);
}
