package team.mephi.adminbot.vaadin.questions.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class AnswerForm extends FormLayout {
    private final TextField id = new TextField();
    private final TextField author = new TextField();
    private final TextField role = new TextField();
    private final TextField direction = new TextField();
    private final TextField text = new TextField();
    private final TextArea answer = new TextArea();
    public AnswerForm() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);

        addFormItem(author, getTranslation("dialog_answer_author_label")).setEnabled(false);
        addFormItem(role, getTranslation("dialog_answer_role_label")).setEnabled(false);
        addFormItem(direction, getTranslation("dialog_answer_direction_label")).setEnabled(false);
        addFormItem(text, getTranslation("dialog_answer_text_label")).setEnabled(false);

        add(new H4(getTranslation("dialog_answer_text_title")));
        add(answer);
    }
}
