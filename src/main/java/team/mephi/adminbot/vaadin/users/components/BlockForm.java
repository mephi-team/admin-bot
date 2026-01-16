package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.textfield.TextArea;
import lombok.Getter;

/**
 * Форма для ввода причины блокировки пользователя.
 */
public class BlockForm extends FormLayout {
    @Getter
    private final TextArea blockReason = new TextArea();

    /**
     * Конструктор формы блокировки пользователя.
     */
    public BlockForm() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setWidthFull();
        setExpandFields(true);
        setExpandColumns(true);

        add(new Hr());
        addFormItem(blockReason, getTranslation("form_user_block_block_reason_label"));
    }
}
