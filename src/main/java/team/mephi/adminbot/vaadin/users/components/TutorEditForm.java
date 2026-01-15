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
import team.mephi.adminbot.dto.CohortDto;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.RoleService;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.vaadin.components.fields.FullNameField;

import java.util.Objects;

/**
 * Форма для редактирования данных куратора.
 */
public class TutorEditForm extends FormLayout {
    @Getter
    private final ComboBox<RoleDto> roles = new ComboBox<>();
    @Getter
    private final FullNameField fullNameField = new FullNameField();
    @Getter
    private final TextField centerOfCompetence = new TextField();
    @Getter
    private final EmailField email = new EmailField();
    @Getter
    private final TextField tgId = new TextField();
    @Getter
    private final ComboBox<CohortDto> cohorts = new ComboBox<>();
    @Getter
    private final MultiSelectComboBox<SimpleDirection> directions = new MultiSelectComboBox<>();
    @Getter
    private final MultiSelectComboBox<SimpleUser> students = new MultiSelectComboBox<>();

    /**
     * Конструктор формы редактирования куратора.
     *
     * @param roleService      сервис для работы с ролями
     * @param cohortService    сервис для работы с когорты
     * @param directionService сервис для работы с направлениями
     * @param userService      сервис для работы с пользователями
     */
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

        cohorts.setItemsPageable(cohortService::getAllCohorts);
        cohorts.setItemLabelGenerator(CohortDto::getName);

        directions.setItemsPageable(directionService::getAllDirections);
        directions.setItemLabelGenerator(SimpleDirection::getName);
        directions.setAutoExpand(MultiSelectComboBox.AutoExpandMode.VERTICAL);

        students.setItems(studentsProvider);
        students.setItemLabelGenerator(u -> u.getFullName() + (Objects.nonNull(u.getTgId()) ? " " + u.getTgId() : ""));
        students.setAutoExpand(MultiSelectComboBox.AutoExpandMode.VERTICAL);

        addFormItem(roles, getTranslation("form_users_roles_label"));
        addFormItem(fullNameField, getTranslation("form_users_full_name_label"));
        addFormItem(centerOfCompetence, getTranslation("form_users_competence_center_label"));
        addFormItem(email, getTranslation("form_users_email_label"));
        addFormItem(tgId, getTranslation("form_users_telegram_label"));
        addFormItem(cohorts, getTranslation("form_users_cohorts_label"));
        addFormItem(directions, getTranslation("form_users_directions_label"));
        addFormItem(students, getTranslation("form_tutor_curatorship_students_label"));
    }
}
