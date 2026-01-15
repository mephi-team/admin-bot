package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

/**
 * Форма для отображения информации о пользователе при блокировке.
 */
public class BlockUserInfo extends FormLayout {
    @Getter
    private final TextField firstName = new TextField();
    @Getter
    private final TextField lastName = new TextField();
    @Getter
    private final TextField tgId = new TextField();

    public BlockUserInfo() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);
        setExpandColumns(true);

        addFormItem(firstName, getTranslation("form_user_block_first_name_label"));
        addFormItem(lastName, getTranslation("form_user_block_last_name_label"));
        addFormItem(tgId, getTranslation("form_user_block_telegram_label"));
    }
}
