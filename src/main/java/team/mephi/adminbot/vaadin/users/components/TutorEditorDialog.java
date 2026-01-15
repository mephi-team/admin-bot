package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.*;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.RoleService;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.vaadin.DialogWithTitle;
import team.mephi.adminbot.vaadin.components.buttons.PrimaryButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.fields.FullNameField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Диалог для создания и редактирования куратора.
 */
public class TutorEditorDialog extends Dialog implements DialogWithTitle {
    private final BeanValidationBinder<SimpleTutor> binder = new BeanValidationBinder<>(SimpleTutor.class);
    private SerializableConsumer<SimpleTutor> onSaveCallback;
    private SimpleTutor user;
    private final Button saveButton = new PrimaryButton(getTranslation("save_button"), ignoredEvent -> onSave());

    public TutorEditorDialog(RoleService roleService, CohortService cohortService, DirectionService directionService, UserService userService) {
        var form = new TutorEditForm(roleService, cohortService, directionService, userService);
        binder.forField(form.getRoles())
                .asRequired()
                .withConverter(RoleDto::getCode, roleCode -> roleService.getByCode(roleCode).orElse(null))
                .bind(SimpleTutor::getRole, SimpleTutor::setRole);
        binder.forField(form.getFullNameField())
                .asRequired()
                .bind(s -> new FullNameField.FullName(s.getFirstName(), s.getLastName()),
                        (s, t) -> {
                            s.setFirstName(t.firstName());
                            s.setLastName(t.lastName());
                        });
        binder.forField(form.getEmail())
                .asRequired()
                .bind(SimpleTutor::getEmail, SimpleTutor::setEmail);
        binder.forField(form.getTgId())
                .asRequired()
                .bind(SimpleTutor::getTgId, SimpleTutor::setTgId);
        binder.forField(form.getCohorts())
                .asRequired()
                .withConverter(CohortDto::getName, cohort -> cohortService.getByName(cohort).orElse(null))
                .bind(SimpleTutor::getCohort, SimpleTutor::setCohort);
        binder.forField(form.getDirections())
                .asRequired()
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
        getFooter().add(new SecondaryButton(getTranslation("cancel_button"), ignoredEvent -> close()), saveButton);

        binder.addStatusChangeListener(e ->
                saveButton.setEnabled(e.getBinder().isValid()));
    }

    @Override
    @SuppressWarnings("unchecked")
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
        if (binder.validate().isOk()) {
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
