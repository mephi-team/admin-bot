package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextArea;

public class BlockUserMessage extends FormLayout {
    private final TextArea message = new TextArea();
    public BlockUserMessage() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);
        setExpandColumns(true);

        add(new H4(getTranslation("form_user_block_last_message_label")), message);
    }
}
