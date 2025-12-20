package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import team.mephi.adminbot.dto.CityDto;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.dto.DirectionDto;
import team.mephi.adminbot.dto.RoleDto;

public class UserForm extends FormLayout {
    private ComboBox<CohortDto> cohorts = new ComboBox<>();
    private ComboBox<DirectionDto> directions = new ComboBox<>();
    private ComboBox<CityDto> cities = new ComboBox<>();
    @Getter
    private ComboBox<RoleDto> roles = new ComboBox<>();
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private EmailField email = new EmailField();
    private TextField telegram = new TextField();
    private TextField phoneNumber = new TextField();
    private TextField id = new TextField();

    public UserForm(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService) {
        roles.setItemsPageable(roleService::getAllRoles);
        roles.setItemLabelGenerator(RoleDto::getDescription);
        roles.setRequiredIndicatorVisible(true);
        cohorts.setItemsPageable(cohortService::getAllCohorts);
        cohorts.setItemLabelGenerator(CohortDto::getName);
        cohorts.setRequiredIndicatorVisible(true);
        directions.setItemsPageable(directionService::getAllDirections);
        directions.setItemLabelGenerator(DirectionDto::getName);
        directions.setRequiredIndicatorVisible(true);
        cities.setItemsPageable(cityService::getAllCities);
        cities.setItemLabelGenerator(CityDto::getName);
        cities.setRequiredIndicatorVisible(true);

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
