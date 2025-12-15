package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

public class UserForm extends FormLayout {
    public UserForm() {
        TextField firstName = new TextField("Имя");
        TextField lastName = new TextField("Фамилия");
        EmailField email = new EmailField("Email");

        setAutoResponsive(true);
        addFormRow(firstName, lastName);
        addFormRow(email);
    }
}
