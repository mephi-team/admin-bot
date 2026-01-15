package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * Фабрика для создания экземпляров TemplateEditorDialog.
 */
@SpringComponent
public class TemplateEditorDialogFactory {
    /**
     * Конструктор фабрики.
     */
    public TemplateEditorDialogFactory() {
    }

    /**
     * Создает новый экземпляр TemplateEditorDialog.
     *
     * @return новый экземпляр TemplateEditorDialog
     */
    public TemplateEditorDialog create() {
        return new TemplateEditorDialog();
    }
}
