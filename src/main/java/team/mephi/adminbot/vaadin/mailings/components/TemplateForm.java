package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

public class TemplateForm extends FormLayout {
    private final TextField id = new TextField();
    @Getter
    private final TextField name = new TextField();
    @Getter
    private final TextArea text = new TextArea();
    public TemplateForm() {
        setAutoResponsive(true);
        setLabelsAside(true);

        addFormItem(name, "Название");
        addFormItem(text, "Текст сообщения");
    }
}
