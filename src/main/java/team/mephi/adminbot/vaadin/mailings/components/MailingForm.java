package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import team.mephi.adminbot.dto.UserDto;

import java.util.List;

public class MailingForm extends FormLayout {
    private TextField id = new TextField();
    private TextField name = new TextField();
    private TextField text = new TextField();
    @Getter
    private ComboBox<UserDto> user = new ComboBox<>();

    public MailingForm(List<UserDto> users) {
        user.setItems(users);
        user.setItemLabelGenerator(UserDto::getUserName);
        user.setRequired(true);
        setAutoResponsive(true);
        setLabelsAside(true);

        addFormItem(name, "Название");
        addFormItem(text, "Описание");
        addFormItem(user, "Пользователь");
    }
}
