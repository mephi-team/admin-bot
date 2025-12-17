package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.components.UserForm;

import java.util.List;

public class UserEditorDialog extends Dialog {
    private final UserForm form = new UserForm(List.of());
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);
    private final Button saveButton = new Button("Сохранить", e -> onSave());
    private final Button cancelButton = new Button("Отмена", e -> close());

    private Runnable onSaveCallback;

    public UserEditorDialog() {
        setHeaderTitle("Пользователь");
        form.setWidth("100%");
        binder.bindInstanceFields(form);
        FormLayout layout = new FormLayout(form);
        layout.setMaxWidth("600px");
        getFooter().add(cancelButton, saveButton);
        add(layout);
    }

    public void openForView(SimpleUser user) {
        binder.readBean(user);
        saveButton.setVisible(false);
        open();
    }

    public void openForEdit(SimpleUser user) {
        binder.readBean(user);
        saveButton.setVisible(true);
        this.onSaveCallback = () -> {
            if (binder.writeBeanIfValid(user)) {
                close();
            }
        };
        open();
    }

    public void openForNew(String role) {
        SimpleUser newUser = new SimpleUser();
        newUser.setRole(role);
        binder.setBean(newUser);
        saveButton.setVisible(true);
        this.onSaveCallback = () -> {
            if (binder.validate().isOk()) {
                close();
            }
        };
        open();
    }

    private void onSave() {
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }

    public SimpleUser getEditedUser() {
        SimpleUser user = new SimpleUser();
        binder.writeBeanIfValid(user);
        return user;
    }

    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }
}
