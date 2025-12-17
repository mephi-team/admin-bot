package team.mephi.adminbot.vaadin.users.service;

import com.vaadin.flow.function.SerializableRunnable;
import team.mephi.adminbot.dto.SimpleUser;

import java.util.List;

public interface UserViewCallback {
    void setOnSaveCallback(SerializableRunnable callback);
    SimpleUser getEditedUser();
    void showUserEditorForView(SimpleUser user);
    void showUserEditorForEdit(SimpleUser user);
    void showUserEditorForNew(String role);
    void confirmDelete(List<Long> ids, Runnable onConfirm);
    void confirmAccept(List<Long> ids, Runnable onConfirm);
    void confirmReject(List<Long> ids, Runnable onConfirm);
    void showNotification(String message);
}
