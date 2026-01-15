package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.CityDto;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.*;
import team.mephi.adminbot.vaadin.core.DialogWithTitle;
import team.mephi.adminbot.vaadin.components.buttons.PrimaryButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.fields.FullNameField;

import java.util.Objects;

/**
 * Диалоговое окно для создания и редактирования пользователей.
 * Использует форму UserForm для ввода данных пользователя и биндер для валидации и связывания данных.
 */
public class UserEditorDialog extends Dialog implements DialogWithTitle {
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);
    private SerializableConsumer<SimpleUser> onSaveCallback;
    private SimpleUser user;
    private final Button saveButton = new PrimaryButton(getTranslation("save_button"), ignoredEvent -> onSave());

    /**
     * Конструктор диалогового окна редактора пользователя.
     *
     * @param roleService      сервис для работы с ролями пользователей.
     * @param cohortService    сервис для работы с когортами.
     * @param directionService сервис для работы с направлениями.
     * @param cityService      сервис для работы с городами.
     * @param tutorService     сервис для работы с кураторами.
     */
    public UserEditorDialog(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService, TutorService tutorService) {
        var form = new UserForm(roleService, cohortService, directionService, cityService, tutorService);
        binder.forField(form.getRoles())
                .asRequired()
                .withConverter(RoleDto::getCode, roleCode -> roleService.getByCode(roleCode).orElse(null))
                .bind(SimpleUser::getRole, SimpleUser::setRole);
        binder.forField(form.getFullNameField())
                .asRequired()
                .bind(
                        s -> new FullNameField.FullName(s.getFirstName(), s.getLastName()),
                        (s, t) -> {
                            s.setFirstName(t.firstName());
                            s.setLastName(t.lastName());
                        }
                );
        binder.forField(form.getEmail()).asRequired().bind(SimpleUser::getEmail, SimpleUser::setEmail);
        binder.forField(form.getTgId()).asRequired().bind(SimpleUser::getTgId, SimpleUser::setTgId);
        binder.forField(form.getPhoneNumber())
                .withConverter(s -> (s != null && !s.isEmpty()) ? s : null, s -> (s != null && !s.isEmpty()) ? s : "")
                .withValidator((value, ignoredContext) -> {
                    if (form.getPhoneNumber().getParent().map(p -> !p.isVisible()).orElse(false)) {
                        return ValidationResult.ok();
                    }
                    return (value != null && !value.isEmpty())
                            ? ValidationResult.ok()
                            : ValidationResult.error("");
                })
                .bind(SimpleUser::getPhoneNumber, SimpleUser::setPhoneNumber);
        binder.forField(form.getCohorts())
                .asRequired()
                .withConverter(CohortDto::getName, cohort -> cohortService.getByName(cohort).orElse(null))
                .bind(SimpleUser::getCohort, SimpleUser::setCohort);
        binder.forField(form.getDirections())
                .asRequired()
                .bind(SimpleUser::getDirection, SimpleUser::setDirection);
        binder.forField(form.getCities())
                .withValidator((value, ignoredContext) -> {
                    if (!form.getCities().getParent().orElseThrow().isVisible()) {
                        return ValidationResult.ok();
                    }
                    form.getCities().setRequiredIndicatorVisible(true);
                    return (value != null)
                            ? ValidationResult.ok()
                            : ValidationResult.error("");
                })
                .withConverter(CityDto::getName, city -> cityService.getByName(city).orElse(new CityDto("", "")))
                .bind(SimpleUser::getCity, SimpleUser::setCity);
        binder.forField(form.getTutor())
                .bind(SimpleUser::getTutor, SimpleUser::setTutor);
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

    /**
     * Обработчик сохранения изменений.
     */
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
