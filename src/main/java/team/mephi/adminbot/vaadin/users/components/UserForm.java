package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import team.mephi.adminbot.dto.RoleDto;

import java.util.List;

public class UserForm extends FormLayout {
    private ComboBox<String> cohorts = new ComboBox<>();
    private ComboBox<String> directions = new ComboBox<>();
    private ComboBox<String> cities = new ComboBox<>();
    @Getter
    private ComboBox<RoleDto> roles = new ComboBox<>();
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private EmailField email = new EmailField();
    private TextField telegram = new TextField();
    private TextField phoneNumber = new TextField();
    private TextField id = new TextField();

    public UserForm(RoleService roleService) {
        roles.setItemsPageable(roleService::getAllRoles);
        roles.setItemLabelGenerator(RoleDto::getDescription);
        roles.setRequiredIndicatorVisible(true);
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

        id.setVisible(false);
        add(id);
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
