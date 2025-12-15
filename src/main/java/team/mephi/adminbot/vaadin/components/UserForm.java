package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;
import java.util.Map;

public class UserForm extends FormLayout {
    public UserForm() {
        ComboBox<String> roles = new ComboBox<>();
        roles.setItems(List.of("test1", "test2"));
        roles.setRequired(true);
        TextField firstName = new TextField();
        firstName.setRequired(true);
        TextField lastName = new TextField();
        lastName.setRequired(true);
        EmailField email = new EmailField();
        email.setRequired(true);
        TextField telegram = new TextField();
        telegram.setRequired(true);

        setAutoResponsive(true);
        setLabelsAside(true);
        setWidthFull();
        setExpandFields(true);
        setExpandColumns(true);
        addFormItem(roles, "Пользователи");
        addFormItem(firstName, "Имя");
        addFormItem(lastName, "Фамилия");
        addFormItem(email, "Email");
        addFormItem(telegram, "Telegram");
    }
}
