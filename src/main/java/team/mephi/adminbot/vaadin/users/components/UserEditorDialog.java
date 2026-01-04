package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.CityDto;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.CityService;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.service.DirectionService;

import java.util.Objects;

public class UserEditorDialog extends Dialog {
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);
    private final Button saveButton = new Button(getTranslation("save_button"), e -> onSave());

    private SerializableConsumer<SimpleUser> onSaveCallback;
    private SimpleUser user;

    public UserEditorDialog(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService) {
        var form = new UserForm(roleService, cohortService, directionService, cityService);
        binder.forField(form.getRoles())
                .withValidator(Objects::nonNull, getTranslation("form_users_roles_validation_message"))
                .withConverter(RoleDto::getCode, roleCode -> roleService.getByCode(roleCode).orElse(null))
                .bind("role");
        binder.forField(form.getCohorts())
                .withValidator(Objects::nonNull, getTranslation("form_users_cohort_validation_message"))
                .withConverter(CohortDto::getName, cohort -> cohortService.getByName(cohort).orElse(null))
                .bind("cohort");
        binder.forField(form.getDirections())
                .withValidator(Objects::nonNull, getTranslation("form_users_direction_validation_message"))
                .bind("direction");
        binder.forField(form.getCities())
                .withValidator(Objects::nonNull, getTranslation("form_users_cities_validation_message"))
                .withConverter(CityDto::getName, city -> cityService.getByName(city).orElse(null))
                .bind("city");
        binder.bindInstanceFields(form);

        setHeaderTitle(getTranslation("dialog_users_new_title"));
        add(form);
        getFooter().add(new Button(getTranslation("cancel_button"), e -> close()), saveButton);

        binder.addStatusChangeListener(e ->
                saveButton.setEnabled(e.getBinder().isValid()));
    }

    public void openForView(SimpleUser user) {
        binder.readBean(user);
        binder.setReadOnly(true);
        saveButton.setVisible(false);
        open();
    }

    public void openForEdit(SimpleUser user, SerializableConsumer<SimpleUser> callback) {
        this.user = user;
        this.onSaveCallback = callback;
        binder.readBean(user);
        binder.setReadOnly(false);
        saveButton.setVisible(true);
        open();
    }

    public void openForNew(String role, SerializableConsumer<SimpleUser> callback) {
        this.onSaveCallback = callback;
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
                binder.writeBeanIfValid(user);
                onSaveCallback.accept(user);
            }
            close();
        }
    }

    @Override
    public void setHeaderTitle(String title) {
        super.setHeaderTitle(getTranslation(title));
    }
}
