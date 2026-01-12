package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

public class TemplateForm extends FormLayout {
    @Getter
    private final TextField name = new TextField();
    @Getter
    private final TextArea text = new TextArea();

    public TemplateForm() {
        setAutoResponsive(true);
        setLabelsAside(true);

        name.addThemeName("neo");
        text.addThemeName("neo");

        addFormItem(name, getTranslation("form_template_name_label"));
        addFormItem(text, getTranslation("form_template_text_label"));
    }
}
