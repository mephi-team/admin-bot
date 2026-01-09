package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.*;
import team.mephi.adminbot.service.*;
import team.mephi.adminbot.vaadin.SimpleDialog;
import team.mephi.adminbot.vaadin.components.FullNameField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class TutorEditorDialog extends Dialog implements SimpleDialog {
    private final BeanValidationBinder<SimpleTutor> binder = new BeanValidationBinder<>(SimpleTutor.class);
    private final Button saveButton = new Button(getTranslation("save_button"), e -> onSave());

    private SerializableConsumer<SimpleTutor> onSaveCallback;
    private SimpleTutor user;

    public TutorEditorDialog(RoleService roleService, CohortService cohortService, DirectionService directionService, UserService userService) {
        var form = new TutorEditForm(roleService, cohortService, directionService, userService);
        binder.forField(form.getFullNameField())
                .bind(s -> new FullNameField.FullName(s.getFirstName(),s.getLastName()),
                        (s, t) -> {s.setFirstName(t.firstName());s.setLastName(t.lastName());});
        binder.forField(form.getRoles())
                .withValidator(Objects::nonNull, getTranslation("form_users_roles_validation_message"))
                .withConverter(RoleDto::getCode, roleCode -> roleService.getByCode(roleCode).orElse(null))
                .bind(SimpleTutor::getRole, SimpleTutor::setRole);
        binder.forField(form.getCohorts())
                .withValidator(Objects::nonNull, getTranslation("form_users_cohort_validation_message"))
                .withConverter(CohortDto::getName, cohort -> cohortService.getByName(cohort).orElse(null))
                .bind(SimpleTutor::getCohort, SimpleTutor::setCohort);
        binder.forField(form.getDirections())
                .withValidator(Objects::nonNull, getTranslation("form_users_direction_validation_message"))
                .withConverter(s -> {
                    if (Objects.isNull(s)) return new ArrayList<SimpleDirection>();
                    return s.stream().toList();
                }, e -> {
                    if (Objects.isNull(e)) return new HashSet<>();
                    return new HashSet<>(e);
                })
                .bind(SimpleTutor::getDirections, SimpleTutor::setDirections);
        binder.forField(form.getStudents())
                .withConverter(s -> {
                    if (Objects.isNull(s)) return new ArrayList<SimpleUser>();
                    return s.stream().toList();
                }, e -> {
                    if (Objects.isNull(e)) return new HashSet<>();
                    return new HashSet<>(e);
                })
                .bind(SimpleTutor::getStudents, SimpleTutor::setStudents);
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
            this.user = SimpleTutor.builder().role((String) user).students(List.of()).build();
        } else {
            this.user = (SimpleTutor) user;
        }
        this.onSaveCallback = (SerializableConsumer<SimpleTutor>) callback;
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
