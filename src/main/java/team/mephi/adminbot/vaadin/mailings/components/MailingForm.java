package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import team.mephi.adminbot.dto.UserDto;

import java.util.List;

public class MailingForm extends FormLayout {
    private final TextField id = new TextField();
    @Getter
    private final TextField name = new TextField();
    private final CheckboxGroup<String> channels = new CheckboxGroup<>();
    @Getter
    private final ComboBox<String> users = new ComboBox<>();
    @Getter
    private final ComboBox<String> cohort = new ComboBox<>();
    private final ComboBox<String> direction = new ComboBox<>();
    private final ComboBox<String> city = new ComboBox<>();
    @Getter
    private final ComboBox<UserDto> user = new ComboBox<>();

    public MailingForm(UserService userService) {
        channels.setItems("Email", "Telegram");

        user.setItemsPageable(userService::getAllUsers);
        user.setItemLabelGenerator(UserDto::getUserName);

        user.setRequiredIndicatorVisible(true);
        cohort.setRequiredIndicatorVisible(true);

        users.setItems(List.of("students", "candidate"));
        cohort.setItems(List.of("summer2025", "winter2025"));
        direction.setItems(List.of("Java", "Python"));
        city.setItems(List.of("Москва", "Омск"));

        setAutoResponsive(true);
        setLabelsAside(true);

        addFormItem(channels, "Канал рассылки");
        addFormItem(users, "Пользователи");
        addFormItem(cohort, "Набор");
        addFormItem(direction, "Направление");
        addFormItem(city, "Город");
        addFormItem(user, "Пользователь");
    }
}
