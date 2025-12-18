package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableRunnable;
import lombok.Setter;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MailingEditorDialog extends Dialog {
    private final BeanValidationBinder<SimpleMailing> binder = new BeanValidationBinder<>(SimpleMailing.class);

    private final Button saveButton = new Button("Сохранить", e -> onSave());
    private final Button cancelButton = new Button("Отмена", e -> close());

    @Setter
    private SerializableRunnable onSaveCallback;

    private final Map<Long, UserDto> userIdToDto;

    public MailingEditorDialog(List<UserDto> users) {
        this.userIdToDto = users.stream()
                .collect(Collectors.toMap(UserDto::getId, Function.identity()));

        var form = new MailingForm(users);
        binder.bindInstanceFields(form);
        binder.forField(form.getUser())
                .withConverter(UserDto::getId, roleCode -> userIdToDto.getOrDefault(roleCode, UserDto.builder().userName("").build()))
                .withValidator(userIdToDto::containsKey, "Пользователь обязателен")
                .bind(SimpleMailing::getUserId, SimpleMailing::setUserId);
        setHeaderTitle("Создание рассылки");
        add(form);
        getFooter().add(cancelButton, saveButton);

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
