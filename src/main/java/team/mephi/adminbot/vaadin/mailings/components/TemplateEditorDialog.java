package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableRunnable;
import lombok.Setter;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.dto.SimpleTemplate;

public class TemplateEditorDialog extends Dialog {
    private final BeanValidationBinder<SimpleTemplate> binder = new BeanValidationBinder<>(SimpleTemplate.class);
    private final Button saveButton = new Button("Сохранить", e -> onSave());

    @Setter
    private SerializableRunnable onSaveCallback;

    public TemplateEditorDialog() {
        var form = new TemplateForm();

        binder.bindInstanceFields(form);

        setHeaderTitle("Создание шаблона");
        add(form);
        getFooter().add(saveButton);
    }

    public void showDialogForNew() {
        var newTemplate = new SimpleTemplate();
        binder.readBean(newTemplate);
        binder.setReadOnly(false);

        open();
    }

    public void showDialogForEdit(SimpleTemplate template) {
        binder.readBean(template);
        binder.setReadOnly(false);

        open();
    }

    public SimpleTemplate getEditedItem() {
        SimpleTemplate template = new SimpleTemplate();
        binder.writeBeanIfValid(template);
        return template;
    }

    private void onSave() {
        if(binder.validate().isOk()) {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            close();
        }
    }
}
