package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableRunnable;
import lombok.Setter;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.dto.SimpleUser;

import java.util.Objects;

public class UserEditorDialog extends Dialog {
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);
    private final Button saveButton = new Button("Сохранить", e -> onSave());

    @Setter
    private SerializableRunnable onSaveCallback;

    public UserEditorDialog(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService) {
        var form = new UserForm(roleService, cohortService, directionService, cityService);
        binder.forField(form.getRoles())
                .withValidator(Objects::nonNull, "Роль обязательна")
                .withConverter(RoleDto::getCode, roleCode -> roleService.getByCode(roleCode).orElse(null))
                .bind("role");
        binder.bindInstanceFields(form);

        setHeaderTitle("Пользователь");
        add(form);
        getFooter().add(new Button("Отмена", e -> close()), saveButton);

        binder.addStatusChangeListener(e ->
                saveButton.setEnabled(e.getBinder().isValid()));
    }

    public void openForView(SimpleUser user) {
        binder.readBean(user);
        binder.setReadOnly(true);
        saveButton.setVisible(false);
        open();
    }

    public void openForEdit(SimpleUser user) {
        binder.readBean(user);
        binder.setReadOnly(false);
        saveButton.setVisible(true);
        open();
    }

    public void openForNew(String role) {
        SimpleUser newUser = new SimpleUser();
        newUser.setRole(role);
        binder.readBean(newUser);
        binder.setReadOnly(false);
        saveButton.setVisible(true);
        open();
    }

    private void onSave() {
        if(binder.validate().isOk()) {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            close();
        }
    }

    public SimpleUser getEditedUser() {
        SimpleUser user = new SimpleUser();
        binder.writeBeanIfValid(user);
        return user;
    }
}
