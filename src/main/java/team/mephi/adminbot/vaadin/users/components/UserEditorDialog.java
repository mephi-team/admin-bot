package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableRunnable;
import lombok.Setter;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.components.UserForm;

import java.util.List;

public class UserEditorDialog extends Dialog {
    private final UserForm form;
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);
    private final Button saveButton = new Button("Сохранить", e -> onSave());
    private final Button cancelButton = new Button("Отмена", e -> close());

    @Setter
    private SerializableRunnable onSaveCallback;

    public UserEditorDialog(RoleService roleService) {
        List<RoleDto> roles = roleService.getAllRoles();
        this.form = new UserForm(roles);
        setHeaderTitle("Пользователь");
        form.setWidth("100%");
        binder.forField(form.getRoles())
                .withConverter(RoleDto::getCode, roleCode -> roleService.getByCode(roleCode).orElseThrow())
                .bind(SimpleUser::getRole, SimpleUser::setRole);
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
        open();
    }

    public void openForNew(String role) {
        SimpleUser newUser = new SimpleUser();
        newUser.setRole(role);
        binder.setBean(newUser);
        saveButton.setVisible(true);
        open();
    }

    private void onSave() {
        if (onSaveCallback != null) {
            onSaveCallback.run();
            close();
        }
    }

    public SimpleUser getEditedUser() {
        SimpleUser user = new SimpleUser();
        binder.writeBeanIfValid(user);
        return user;
    }
}
