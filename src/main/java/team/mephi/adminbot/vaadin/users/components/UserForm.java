package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.*;
import team.mephi.adminbot.service.*;
import team.mephi.adminbot.vaadin.components.FullNameField;

public class UserForm extends FormLayout {
    @Getter private final ComboBox<RoleDto> roles = new ComboBox<>();
    @Getter private final FullNameField fullNameField = new FullNameField();
    @Getter private final EmailField email = new EmailField();
    @Getter private final TextField tgId = new TextField();
    @Getter private final TextField phoneNumber = new TextField();
    @Getter private final ComboBox<CohortDto> cohorts = new ComboBox<>();
    @Getter private final ComboBox<SimpleDirection> directions = new ComboBox<>();
    @Getter private final ComboBox<CityDto> cities = new ComboBox<>();
    @Getter private final ComboBox<SimpleTutor> tutor = new ComboBox<>();

    public UserForm(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService, TutorService tutorService) {
        var tutorProvider = new CallbackDataProvider<SimpleTutor, String>(
                query -> {
                    Pageable pageable = PageRequest.of(query.getOffset() / query.getLimit(), query.getLimit());
                    return tutorService.findAllByName(query.getFilter().orElse(""), pageable);
                },
                query -> tutorService.countByName(query.getFilter().orElse(""))
        );

        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);
        setExpandColumns(true);

        roles.setItemsPageable(roleService::getAllRoles);
        roles.setItemLabelGenerator(RoleDto::getName);

        cohorts.setItemsPageable(cohortService::getAllCohorts);
        cohorts.setItemLabelGenerator(CohortDto::getName);

        directions.setItemsPageable(directionService::getAllDirections);
        directions.setItemLabelGenerator(SimpleDirection::getName);

        cities.setItemsPageable(cityService::getAllCities);
        cities.setItemLabelGenerator(CityDto::getName);

        tutor.setItems(tutorProvider);
        tutor.setItemLabelGenerator(SimpleTutor::getFullName);

        addFormItem(roles, getTranslation("form_users_roles_label"));
        addFormItem(fullNameField, getTranslation("form_users_full_name_label"));
        addFormItem(email, getTranslation("form_users_email_label"));
        addFormItem(tgId, getTranslation("form_users_telegram_label"));
        FormItem phoneForm = addFormItem(phoneNumber, getTranslation("form_users_phone_number_label"));
        addFormItem(cohorts, getTranslation("form_users_cohorts_label"));
        addFormItem(directions, getTranslation("form_users_directions_label"));
        FormItem cityForm = addFormItem(cities, getTranslation("form_users_cities_label"));
        FormItem tutorForm = addFormItem(tutor, getTranslation("form_users_tutor_label"));

        roles.addValueChangeListener(e -> {
            phoneForm.setVisible(!"LC_EXPERT".equals(e.getValue().getCode()));
            cityForm.setVisible(!"LC_EXPERT".equals(e.getValue().getCode()));
            tutorForm.setVisible("STUDENT".equals(e.getValue().getCode()));
        });
    }
}
