package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.UserService;

public class TutorForm extends FormLayout {
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private TextField tgId = new TextField();
    private ComboBox<SimpleDirection> direction = new ComboBox<>();

    public TutorForm(UserService userService) {
        var provider = new CallbackDataProvider<SimpleUser, String>(
                query -> {
                    Pageable pageable = PageRequest.of(query.getOffset() / query.getLimit(), query.getLimit());
                    return userService.findAllByRoleAndName("student", query.getFilter().orElse(""), pageable);
                },
                query -> userService.countByRoleAndName("student", query.getFilter().orElse("")),
                SimpleUser::getId
        );

        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);

        MultiSelectComboBox<SimpleUser> comboBox = new MultiSelectComboBox<>();
        comboBox.setItems(provider);
        comboBox.setItemLabelGenerator(SimpleUser::getFullName);
        comboBox.setAutoExpand(MultiSelectComboBox.AutoExpandMode.VERTICAL);

        addFormItem(firstName, getTranslation("form_tutor_curatorship_first_name_label"));
        addFormItem(lastName, getTranslation("form_tutor_curatorship_last_name_label"));
        addFormItem(tgId, getTranslation("form_tutor_curatorship_telegram_label"));
        addFormItem(direction, getTranslation("form_tutor_curatorship_direction_label"));
        addFormItem(comboBox, getTranslation("form_tutor_curatorship_students_label"));
    }
}
