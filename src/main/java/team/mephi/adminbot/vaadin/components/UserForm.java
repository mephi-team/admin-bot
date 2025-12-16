package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import team.mephi.adminbot.model.Role;

import java.util.List;

public class UserForm extends FormLayout {
    ComboBox<String> cohorts = new ComboBox<>();
    ComboBox<String> directions = new ComboBox<>();
    ComboBox<String> cities = new ComboBox<>();
    public ComboBox<Role> roles = new ComboBox<>();
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private EmailField email = new EmailField();
    private TextField telegram = new TextField();
    private TextField phoneNumber = new TextField();

    public UserForm(List<Role> roleList) {
        roles.setItems(roleList);
        roles.setRequired(true);
        roles.setItemLabelGenerator(Role::getDescription);
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
