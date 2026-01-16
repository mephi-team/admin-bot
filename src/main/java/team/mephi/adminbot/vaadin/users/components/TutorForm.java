package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.vaadin.components.fields.FullNameField;

import java.util.Objects;

/**
 * Форма для отображения информации о кураторе и его подопечных.
 */
public class TutorForm extends FormLayout {
    @Getter
    private final FullNameField fullNameField = new FullNameField();
    @Getter
    final private TextField tgId = new TextField();
    @Getter
    private final MultiSelectComboBox<SimpleDirection> directions = new MultiSelectComboBox<>();
    @Getter
    private final MultiSelectComboBox<SimpleUser> students = new MultiSelectComboBox<>();

    /**
     * Конструктор формы кураторов.
     *
     * @param userService      сервис для работы с пользователями.
     * @param directionService сервис для работы с направлениями.
     */
    public TutorForm(UserService userService, DirectionService directionService) {
        var directionsProvider = new CallbackDataProvider<SimpleDirection, String>(
                query -> {
                    Pageable pageable = PageRequest.of(query.getOffset() / query.getLimit(), query.getLimit());
                    return directionService.getAllDirections(pageable, query.getFilter().orElse("")).stream();
                },
                query -> directionService.countAllDirections(query.getFilter().orElse(""))
        );
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

        fullNameField.setReadOnly(true);
        tgId.setReadOnly(true);

        directions.setReadOnly(true);
        directions.setItems(directionsProvider);
        directions.setItemLabelGenerator(SimpleDirection::getName);
        directions.setAutoExpand(MultiSelectComboBox.AutoExpandMode.VERTICAL);

        students.setItems(studentsProvider);
        students.setItemLabelGenerator(u -> u.getFullName() + (Objects.nonNull(u.getTgId()) ? " " + u.getTgId() : ""));
        students.setAutoExpand(MultiSelectComboBox.AutoExpandMode.VERTICAL);
        students.setAutofocus(true);
        students.addSelectionListener(e -> {
            var selectedItems = e.getAddedSelection();
            boolean hasInvalid = selectedItems.removeIf(item -> Objects.isNull(item.getId()));
            if (hasInvalid) {
                students.setValue(selectedItems);
            }
        });

        addFormItem(fullNameField, getTranslation("form_tutor_curatorship_full_name_label"));
        addFormItem(tgId, getTranslation("form_tutor_curatorship_telegram_label"));
        addFormItem(directions, getTranslation("form_tutor_curatorship_direction_label"));
        addFormItem(students, getTranslation("form_tutor_curatorship_students_label"));
    }
}
