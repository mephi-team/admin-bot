package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import team.mephi.adminbot.dto.SimpleUser;

public class MailingEditorDialog extends Dialog {
    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);

    private final Button saveButton = new Button("Сохранить", e -> onSave());
    private final Button cancelButton = new Button("Отмена", e -> close());

    public MailingEditorDialog() {
        var form = new MailingForm();
        binder.bindInstanceFields(form);

        setHeaderTitle("Создание рассылки");
        add(new MailingForm());
        getFooter().add(cancelButton, saveButton);
    }

    private void onSave() {
        if(binder.validate().isOk()) {
            close();
        }
    }
}
