package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.UserService;

import java.util.Objects;

public class TutorForm extends FormLayout {
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private TextField tgId = new TextField();
    private ComboBox<SimpleDirection> direction = new ComboBox<>();
    @Getter
    private MultiSelectComboBox<SimpleUser> comboBox = new MultiSelectComboBox<>();

    public TutorForm(UserService userService) {
        var provider = new CallbackDataProvider<SimpleUser, String>(
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

        firstName.setReadOnly(true);
        lastName.setReadOnly(true);
        tgId.setReadOnly(true);
        direction.setReadOnly(true);

        comboBox.setItems(provider);
        comboBox.setItemLabelGenerator(u -> u.getFullName() + (Objects.nonNull(u.getTgId()) ? " @" + u.getTgId() : ""));
        comboBox.setAutoExpand(MultiSelectComboBox.AutoExpandMode.VERTICAL);
        comboBox.addSelectionListener(e -> {
            var selectedItems = e.getAddedSelection();
            boolean hasInvalid = selectedItems.removeIf(item -> Objects.isNull(item.getId()));
            if (hasInvalid) {
                comboBox.setValue(selectedItems);
            }
        });

        addFormItem(firstName, getTranslation("form_tutor_curatorship_first_name_label"));
        addFormItem(lastName, getTranslation("form_tutor_curatorship_last_name_label"));
        addFormItem(tgId, getTranslation("form_tutor_curatorship_telegram_label"));
        addFormItem(direction, getTranslation("form_tutor_curatorship_direction_label"));
        addFormItem(comboBox, getTranslation("form_tutor_curatorship_students_label"));
    }
}
