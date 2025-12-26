package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

public class TutorForm extends FormLayout {
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();

    public TutorForm() {
        addFormItem(firstName, "Имя");
        addFormItem(lastName, "Фамилия");
    }
}
