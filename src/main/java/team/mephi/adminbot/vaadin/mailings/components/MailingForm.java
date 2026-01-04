package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import team.mephi.adminbot.dto.*;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.service.CityService;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.vaadin.users.components.RoleService;

public class MailingForm extends FormLayout {
    @Getter
    private final TextField name = new TextField();
    private final CheckboxGroup<String> channels = new CheckboxGroup<>();
    @Getter
    private final ComboBox<RoleDto> users = new ComboBox<>();
    @Getter
    private final ComboBox<CohortDto> cohort = new ComboBox<>();
    @Getter
    private final ComboBox<SimpleDirection> direction = new ComboBox<>();
    @Getter
    private final ComboBox<CityDto> city = new ComboBox<>();
    @Getter
    private final ComboBox<UserDto> curator = new ComboBox<>();

    public MailingForm(UserService userService, RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService) {
        setAutoResponsive(true);
        setLabelsAside(true);

        channels.setItems("Email", "Telegram");

        users.setItemsPageable(roleService::getAllRoles);
        users.setItemLabelGenerator(RoleDto::getName);
        users.setRequiredIndicatorVisible(true);

        cohort.setItemsPageable(cohortService::getAllCohorts);
        cohort.setItemLabelGenerator(CohortDto::getName);
        cohort.setRequiredIndicatorVisible(true);

        direction.setItemsPageable(directionService::getAllDirections);
        direction.setItemLabelGenerator(SimpleDirection::getName);
        direction.setRequiredIndicatorVisible(true);

        city.setItemsPageable(cityService::getAllCities);
        city.setItemLabelGenerator(CityDto::getName);
        city.setRequiredIndicatorVisible(true);

        curator.setItemsPageable(userService::findAllCurators);
        curator.setItemLabelGenerator(UserDto::getUserName);
        curator.setRequiredIndicatorVisible(true);

        addFormItem(channels, getTranslation("form_mailing_channels_label"));
        addFormItem(users, getTranslation("form_mailing_users_label"));
        addFormItem(cohort, getTranslation("form_mailing_cohort_label"));
        addFormItem(direction, getTranslation("form_mailing_direction_label"));
        addFormItem(city, getTranslation("form_mailing_city_label"));
        addFormItem(curator, getTranslation("form_mailing_curator_label"));

        Accordion accordion = new Accordion();

        Span name = new Span("Список выбранных получателей соответствует заданным выше фильтрам");

        MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
        listBox.setItems("Test Text 1", "Test Text 2", "Test Text 3");
        listBox.select("Test Text 1", "Test Text 3");
        FormItem box = addFormItem(listBox, getTranslation("form_mailing_first_name_last_name_label"));

        VerticalLayout personalInformationLayout = new VerticalLayout(name, box);
        personalInformationLayout.setSpacing(false);
        personalInformationLayout.setPadding(false);

        accordion.add(new AccordionPanel(new Span(new Span(getTranslation("form_mailing_accordion_label")), new Span(" (5)")), personalInformationLayout));
        accordion.close();

        add(accordion);
    }
}
