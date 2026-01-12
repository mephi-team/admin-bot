package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class BlockForm extends FormLayout {
    private TextArea blockReason = new TextArea();

    public BlockForm() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setWidthFull();
        setExpandFields(true);
        setExpandColumns(true);

        blockReason.addThemeName("neo");

        addFormItem(blockReason, getTranslation("form_user_block_block_reason_label"));
    }
}
