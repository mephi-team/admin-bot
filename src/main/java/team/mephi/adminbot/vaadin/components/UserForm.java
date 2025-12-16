package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class UserForm extends FormLayout {
    private ComboBox<String> roles = new ComboBox<>();
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private EmailField email = new EmailField();
    private TextField telegram = new TextField();
    private TextField phoneNumber = new TextField();
    ComboBox<String> cohorts = new ComboBox<>();
    ComboBox<String> directions = new ComboBox<>();
    ComboBox<String> cities = new ComboBox<>();

    public UserForm() {
        roles.setItems(List.of("test1", "test2"));
        roles.setRequired(true);
        cohorts.setItems(List.of("test1", "test2"));
        cohorts.setRequired(true);
        directions.setItems(List.of("test1", "test2"));
        directions.setRequired(true);
        cities.setItems(List.of("test1", "test2"));
        cities.setRequired(true);

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
        addFormItem(phoneNumber, "Телефон");
        addFormItem(cohorts, "Набор");
        addFormItem(directions, "Направление");
        addFormItem(cities, "Город");
    }
}
