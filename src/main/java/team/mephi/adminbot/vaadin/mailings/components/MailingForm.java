package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

public class MailingForm extends FormLayout {
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();

    public MailingForm() {

        setAutoResponsive(true);
        setLabelsAside(true);

        addFormItem(firstName, "Имя");
        addFormItem(lastName, "Фамилия");
    }
}
