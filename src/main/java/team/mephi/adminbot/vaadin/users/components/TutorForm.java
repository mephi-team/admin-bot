package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.textfield.TextField;

public class TutorForm extends FormLayout {
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private TextField telegram = new TextField();
    private TextField direction = new TextField();

    public TutorForm() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setWidthFull();
        setExpandFields(true);
        setExpandColumns(true);

        addFormItem(firstName, getTranslation("form_tutor_curatorship_first_name_label"));
        addFormItem(lastName, getTranslation("form_tutor_curatorship_last_name_label"));
        addFormItem(telegram, getTranslation("form_tutor_curatorship_telegram_label"));
        addFormItem(direction, getTranslation("form_tutor_curatorship_direction_label"));

        MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
        listBox.setItems("Test Text 1", "Test Text 2", "Test Text 3");
        listBox.select("Test Text 1", "Test Text 3");
        FormItem box = addFormItem(listBox, getTranslation("form_tutor_curatorship_students_label"));

        add(box);
    }
}
