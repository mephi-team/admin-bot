package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
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

import java.util.Objects;

public class TutorEditForm extends FormLayout {
    @Getter
    private ComboBox<RoleDto> roles = new ComboBox<>();
    @Getter
    private FullNameField fullNameField = new FullNameField();
//    private TextField firstName = new TextField();
//    private TextField lastName = new TextField();
    private TextField centerOfCompetence = new TextField();
    private EmailField email = new EmailField();
    private TextField tgId = new TextField();
    @Getter
    private ComboBox<CohortDto> cohorts = new ComboBox<>();
    @Getter
    private MultiSelectComboBox<SimpleDirection> directions = new MultiSelectComboBox<>();
    @Getter
    private final MultiSelectComboBox<SimpleUser> students = new MultiSelectComboBox<>();

    public TutorEditForm(RoleService roleService, CohortService cohortService, DirectionService directionService, UserService userService) {
        var studentsProvider = new CallbackDataProvider<SimpleUser, String>(
                query -> {
                    Pageable pageable = PageRequest.of(query.getOffset() / query.getLimit(), query.getLimit());
                    return userService.findAllForCuratorship(query.getFilter().orElse(""), pageable);
                },
                query -> userService.countAllForCuratorship(query.getFilter().orElse("")),
                SimpleUser::getId
        );

        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);
        setExpandColumns(true);

        roles.setItemsPageable(roleService::getAllRoles);
        roles.setItemLabelGenerator(RoleDto::getName);
        roles.setRequiredIndicatorVisible(true);
        cohorts.setItemsPageable(cohortService::getAllCohorts);
        cohorts.setItemLabelGenerator(CohortDto::getName);
        cohorts.setRequiredIndicatorVisible(true);
        directions.setItemsPageable(directionService::getAllDirections);
        directions.setItemLabelGenerator(SimpleDirection::getName);
        directions.setAutoExpand(MultiSelectComboBox.AutoExpandMode.VERTICAL);
        directions.setRequiredIndicatorVisible(true);
        students.setItems(studentsProvider);
        students.setItemLabelGenerator(u -> u.getFullName() + (Objects.nonNull(u.getTgId()) ? " @" + u.getTgId() : ""));
        students.setAutoExpand(MultiSelectComboBox.AutoExpandMode.VERTICAL);
        students.setRequiredIndicatorVisible(true);

        addFormItem(roles, getTranslation("form_users_roles_label"));
        addFormItem(fullNameField, getTranslation("form_users_full_name_label"));
//        addFormItem(firstName, getTranslation("form_users_first_name_label"));
//        addFormItem(lastName, getTranslation("form_users_last_name_label"));
        addFormItem(centerOfCompetence, getTranslation("form_users_competence_center_label"));
        addFormItem(email, getTranslation("form_users_email_label"));
        addFormItem(tgId, getTranslation("form_users_telegram_label"));
        addFormItem(cohorts, getTranslation("form_users_cohorts_label"));
        addFormItem(directions, getTranslation("form_users_directions_label"));
        addFormItem(students, getTranslation("form_users_cities_label"));
    }
}
