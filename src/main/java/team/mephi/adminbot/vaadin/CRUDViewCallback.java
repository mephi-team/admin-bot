package team.mephi.adminbot.vaadin;

import com.vaadin.flow.function.SerializableConsumer;

import java.util.List;

public interface CRUDViewCallback<T> {
    void showDialogForView(T user);
    void showDialogForNew(String type, SerializableConsumer<?> callback);
    void showDialogForEdit(Object item, SerializableConsumer<?> callback);
    void confirmDelete(List<Long> ids, Runnable onConfirm);
}
