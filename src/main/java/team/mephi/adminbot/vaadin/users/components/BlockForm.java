package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import lombok.Getter;

public class BlockForm extends FormLayout {
    @Getter
    private final TextArea blockReason = new TextArea();

    public BlockForm() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setWidthFull();
        setExpandFields(true);
        setExpandColumns(true);

        TextArea blockReason = new TextArea();
        addFormItem(blockReason, getTranslation("form_user_block_block_reason_label"));
    }
}
