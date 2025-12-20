package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import team.mephi.adminbot.dto.UserDto;

public class MailingForm extends FormLayout {
    private final TextField id = new TextField();
    @Getter
    private final TextField name = new TextField();
    @Getter
    private final TextField text = new TextField();
    @Getter
    private final ComboBox<UserDto> user = new ComboBox<>();

    public MailingForm(UserService userService) {
        user.setItemsPageable(userService::getAllUsers);
        user.setItemLabelGenerator(UserDto::getUserName);
        user.setRequiredIndicatorVisible(true);

        setAutoResponsive(true);
        setLabelsAside(true);

        addFormItem(name, "Название");
        addFormItem(text, "Описание");
        addFormItem(user, "Пользователь");
    }
}
