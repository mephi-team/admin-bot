package team.mephi.adminbot.vaadin;

import com.vaadin.flow.function.SerializableRunnable;

import java.util.List;

public class CRUDViewCallbackBase<T> implements CRUDViewCallback<T> {
    @Override
    public void setOnSaveCallback(SerializableRunnable callback) {

    }

    @Override
    public T getEditedItem() {
        return null;
    }

    @Override
    public void showDialogForView(T user) {

    }

    @Override
    public void showDialogForNew(String role) {

    }

    @Override
    public void showDialogForEdit(T user) {

    }

    @Override
    public void showNotificationForNew() {

    }

    @Override
    public void showNotificationForEdit(Long id) {

    }

    @Override
    public void showNotificationForDelete(List<Long> ids) {

    }

    @Override
    public void confirmDelete(List<Long> ids, Runnable onConfirm) {

    }
}
