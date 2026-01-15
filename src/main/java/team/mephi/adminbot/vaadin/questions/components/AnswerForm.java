package team.mephi.adminbot.vaadin.questions.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

/**
 * Форма для ответа на вопрос.
 */
public class AnswerForm extends FormLayout {
    @Getter
    private final TextField author = new TextField();
    @Getter
    private final TextField role = new TextField();
    @Getter
    private final TextField direction = new TextField();
    @Getter
    private final TextField text = new TextField();
    @Getter
    private final TextArea answer = new TextArea();

    /**
     * Конструктор формы для ответа на вопрос.
     */
    public AnswerForm() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);
        setExpandColumns(true);

        author.setReadOnly(true);
        role.setReadOnly(true);
        direction.setReadOnly(true);
        text.setReadOnly(true);
        answer.setMinRows(4);

        addFormItem(author, getTranslation("dialog_answer_author_label"));
        addFormItem(role, getTranslation("dialog_answer_role_label"));
        addFormItem(direction, getTranslation("dialog_answer_direction_label"));
        addFormItem(text, getTranslation("dialog_answer_text_label"));

        add(new H4(getTranslation("dialog_answer_text_title")));
        add(answer);
    }
}
