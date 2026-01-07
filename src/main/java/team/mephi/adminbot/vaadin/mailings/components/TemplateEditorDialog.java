package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.vaadin.SimpleDialog;

import java.util.Objects;

public class TemplateEditorDialog extends Dialog implements SimpleDialog<SimpleTemplate> {
    private final BeanValidationBinder<SimpleTemplate> binder = new BeanValidationBinder<>(SimpleTemplate.class);
    private final Button saveButton = new Button(getTranslation("save_button"), e -> onSave());

    private SerializableConsumer<SimpleTemplate> onSaveCallback;

    public TemplateEditorDialog() {
        var form = new TemplateForm();

        binder.bindInstanceFields(form);

        setHeaderTitle("dialog_template_new_title");
        add(form);
        getFooter().add(saveButton);
    }

    public void showDialog(Object template, SerializableConsumer<SimpleTemplate> callback) {
        if (Objects.isNull(template)) template = new SimpleTemplate();
        this.onSaveCallback = callback;
        binder.readBean((SimpleTemplate) template);
        binder.setReadOnly(false);

        open();
    }

    private void onSave() {
        if(binder.validate().isOk()) {
            if (onSaveCallback != null) {
                SimpleTemplate template = new SimpleTemplate();
                binder.writeBeanIfValid(template);
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
