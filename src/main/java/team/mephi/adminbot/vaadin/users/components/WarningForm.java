package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class WarningForm extends FormLayout {
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private TextField telegram = new TextField();
    private TextArea text = new TextArea();

    public WarningForm() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setWidthFull();
        setExpandFields(true);
        setExpandColumns(true);

        addFormItem(firstName, getTranslation("form_user_block_first_name_label"));
        addFormItem(lastName, getTranslation("form_user_block_last_name_label"));
        addFormItem(telegram, getTranslation("form_user_block_telegram_label"));
        addFormItem(text, getTranslation("form_user_block_warning_reason_label"));
        add(new H4(getTranslation("form_user_block_last_message_label")));
        add(new TextArea());
    }
}
