package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

public class UserDeleteDialog extends ConfirmDialog {
    public UserDeleteDialog(ComponentEventListener<ConfirmEvent> confirmListener) {
        setHeader("Удалить пользователя?");
        setText(
                "Вы действительно хотите удалить пользователя?");
        setCancelable(true);
        setCancelText("Отмена");
        addCancelListener(event -> {
            close();
        });
        setConfirmText("Удалить");
        addConfirmListener(confirmListener);
    }
}
