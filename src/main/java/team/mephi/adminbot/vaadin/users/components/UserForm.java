package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import team.mephi.adminbot.dto.CityDto;
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.service.CityService;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.RoleService;

public class UserForm extends FormLayout {
    @Getter
    private ComboBox<RoleDto> roles = new ComboBox<>();
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private EmailField email = new EmailField();
    private TextField tgId = new TextField();
    private TextField phoneNumber = new TextField();
    @Getter
    private ComboBox<CohortDto> cohorts = new ComboBox<>();
    @Getter
    private ComboBox<SimpleDirection> directions = new ComboBox<>();
    @Getter
    private ComboBox<CityDto> cities = new ComboBox<>();

    public UserForm(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService) {
        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);

        roles.setItemsPageable(roleService::getAllRoles);
        roles.setItemLabelGenerator(RoleDto::getName);
        roles.setRequiredIndicatorVisible(true);
        cohorts.setItemsPageable(cohortService::getAllCohorts);
        cohorts.setItemLabelGenerator(CohortDto::getName);
        cohorts.setRequiredIndicatorVisible(true);
        directions.setItemsPageable(directionService::getAllDirections);
        directions.setItemLabelGenerator(SimpleDirection::getName);
        directions.setRequiredIndicatorVisible(true);
        cities.setItemsPageable(cityService::getAllCities);
        cities.setItemLabelGenerator(CityDto::getName);
        cities.setRequiredIndicatorVisible(true);

        addFormItem(roles, getTranslation("form_users_roles_label"));
        addFormItem(firstName, getTranslation("form_users_first_name_label"));
        addFormItem(lastName, getTranslation("form_users_last_name_label"));
        addFormItem(email, getTranslation("form_users_email_label"));
        addFormItem(tgId, getTranslation("form_users_telegram_label"));
        addFormItem(phoneNumber, getTranslation("form_users_phone_number_label"));
        addFormItem(cohorts, getTranslation("form_users_cohorts_label"));
        addFormItem(directions, getTranslation("form_users_directions_label"));
        addFormItem(cities, getTranslation("form_users_cities_label"));
    }
}
