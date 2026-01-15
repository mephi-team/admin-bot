package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * Фабрика для создания экземпляров TemplateEditorDialog.
 */
@SpringComponent
public class TemplateEditorDialogFactory {
    public TemplateEditorDialogFactory() {
    }

    public TemplateEditorDialog create() {
        return new TemplateEditorDialog();
    }
}
