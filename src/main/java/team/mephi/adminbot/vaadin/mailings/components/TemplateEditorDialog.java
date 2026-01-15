package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.vaadin.DialogWithTitle;
import team.mephi.adminbot.vaadin.components.buttons.PrimaryButton;

import java.util.Objects;

/**
 * Диалоговое окно для создания и редактирования шаблонов.
 */
public class TemplateEditorDialog extends Dialog implements DialogWithTitle {
    private final BeanValidationBinder<SimpleTemplate> binder = new BeanValidationBinder<>(SimpleTemplate.class);
    private SerializableConsumer<SimpleTemplate> onSaveCallback;
    private SimpleTemplate template;

    public TemplateEditorDialog() {
        var form = new TemplateForm();

        binder.bindInstanceFields(form);

        setHeaderTitle("dialog_template_new_title");
        add(form);
        Button saveButton = new PrimaryButton(getTranslation("save_button"), ignoredEvent -> onSave());
        getFooter().add(saveButton);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void showDialog(Object template, SerializableConsumer<?> callback) {
        this.template = Objects.isNull(template) ? new SimpleTemplate() : (SimpleTemplate) template;
        this.onSaveCallback = (SerializableConsumer<SimpleTemplate>) callback;
        binder.readBean((SimpleTemplate) template);
        binder.setReadOnly(false);

        open();
    }

    private void onSave() {
        if (binder.validate().isOk()) {
            if (onSaveCallback != null) {
                binder.writeBeanIfValid(this.template);
                onSaveCallback.accept(template);
            }
            close();
        }
    }

    @Override
    public void setHeaderTitle(String title) {
        super.setHeaderTitle(getTranslation(title));
    }
}
