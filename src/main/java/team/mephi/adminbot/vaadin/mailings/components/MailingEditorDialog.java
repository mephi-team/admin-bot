package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableRunnable;
import lombok.Setter;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.dto.UserDto;

import java.util.Objects;

public class MailingEditorDialog extends Dialog {
    private final BeanValidationBinder<SimpleMailing> binder = new BeanValidationBinder<>(SimpleMailing.class);
    private final Button saveButton = new Button("Сохранить", e -> onSave());

    @Setter
    private SerializableRunnable onSaveCallback;

    public MailingEditorDialog(UserService userService) {
        var form = new MailingForm(userService);
        binder.forField(form.getUser())
                .withValidator(Objects::nonNull, "Пользователь обязателен")
                .withConverter(UserDto::getId, userId -> userService.getById(userId).orElse(null))
                .bind("userId");
        binder.bindInstanceFields(form);

        setHeaderTitle("Создание рассылки");
        add(form);
        getFooter().add(new Button("Отмена", e -> close()), saveButton);

        binder.addStatusChangeListener(e ->
                saveButton.setEnabled(e.getBinder().isValid()));
    }

    public void openForNew() {
        var newMailing = new SimpleMailing();
        newMailing.setUserId(0L);
        binder.readBean(newMailing);
        binder.setReadOnly(false);
        saveButton.setVisible(true);
        open();
    }

    public void openForEdit(SimpleMailing mailing) {
        binder.readBean(mailing);
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

    public SimpleMailing getEditedMailing() {
        SimpleMailing mailing = new SimpleMailing();
        binder.writeBeanIfValid(mailing);
        return mailing;
    }
}
