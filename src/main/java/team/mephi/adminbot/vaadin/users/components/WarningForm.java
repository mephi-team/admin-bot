package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class WarningForm extends FormLayout {
    private TextArea warningReason = new TextArea();

    public WarningForm() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setWidthFull();
        setExpandFields(true);
        setExpandColumns(true);

        addFormItem(warningReason, getTranslation("form_user_block_warning_reason_label"));
    }
}
