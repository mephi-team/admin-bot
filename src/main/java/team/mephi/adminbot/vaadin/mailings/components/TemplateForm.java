package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

/**
 * Форма для создания и редактирования шаблонов рассылок.
 */
public class TemplateForm extends FormLayout {
    @Getter
    private final TextField name = new TextField();
    @Getter
    private final TextArea text = new TextArea();

    /**
     * Конструктор формы шаблона.
     */
    public TemplateForm() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);
        setExpandColumns(true);

        name.setAutofocus(true);
        text.setMinRows(10);

        addFormItem(name, getTranslation("form_template_name_label"));
        add(new H4(getTranslation("form_template_text_label")), new Hr());
        add(text);
    }
}
