package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.*;
import team.mephi.adminbot.service.*;
import team.mephi.adminbot.vaadin.SimpleDialog;

import java.util.Objects;

public class UserEditorDialog extends Dialog implements SimpleDialog {
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);
    private final Button saveButton = new Button(getTranslation("save_button"), e -> onSave());

    private SerializableConsumer<SimpleUser> onSaveCallback;
    private SimpleUser user;

    public UserEditorDialog(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService, TutorService tutorService) {
        var form = new UserForm(roleService, cohortService, directionService, cityService, tutorService);
        binder.forField(form.getRoles())
                .withValidator(Objects::nonNull, getTranslation("form_users_roles_validation_message"))
                .withConverter(RoleDto::getCode, roleCode -> roleService.getByCode(roleCode).orElse(null))
                .bind(SimpleUser::getRole, SimpleUser::setRole);
        binder.forField(form.getCohorts())
                .withValidator(Objects::nonNull, getTranslation("form_users_cohort_validation_message"))
                .withConverter(CohortDto::getName, cohort -> cohortService.getByName(cohort).orElse(null))
                .bind(SimpleUser::getCohort, SimpleUser::setCohort);
        binder.forField(form.getDirections())
                .withValidator(Objects::nonNull, getTranslation("form_users_direction_validation_message"))
                .bind(SimpleUser::getDirection, SimpleUser::setDirection);
        binder.forField(form.getCities())
                .withValidator(Objects::nonNull, getTranslation("form_users_cities_validation_message"))
                .withConverter(CityDto::getName, city -> cityService.getByName(city).orElse(null))
                .bind(SimpleUser::getCity, SimpleUser::setCity);
        binder.forField(form.getTutor())
                .bind(SimpleUser::getTutor, SimpleUser::setTutor);
        binder.bindInstanceFields(form);

        setHeaderTitle("dialog_users_created_title");
        add(form);
        setWidth("100%");
        setMaxWidth("500px");
        getFooter().add(new Button(getTranslation("cancel_button"), e -> close()), saveButton);

        binder.addStatusChangeListener(e ->
                saveButton.setEnabled(e.getBinder().isValid()));
    }

    @Override
    public void showDialog(Object user, SerializableConsumer<?> callback) {
        if (user instanceof String) {
            this.user = SimpleUser.builder().role((String) user).build();
        } else {
            this.user = (SimpleUser) user;
        }
        this.onSaveCallback = (SerializableConsumer<SimpleUser>) callback;
        binder.readBean(this.user);
        binder.setReadOnly(Objects.isNull(callback));
        saveButton.setVisible(Objects.nonNull(callback));
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
