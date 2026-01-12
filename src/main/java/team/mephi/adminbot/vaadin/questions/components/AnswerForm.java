package team.mephi.adminbot.vaadin.questions.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class AnswerForm extends FormLayout {
    private final TextField author = new TextField();
    private final TextField role = new TextField();
    private final TextField direction = new TextField();
    private final TextField text = new TextField();
    private final TextArea answer = new TextArea();

    public AnswerForm() {
        setAutoResponsive(true);
        setLabelsAside(true);
        setExpandFields(true);
        setExpandColumns(true);

        author.setReadOnly(true);
        author.addThemeName("neo");
        role.setReadOnly(true);
        role.addThemeName("neo");
        direction.setReadOnly(true);
        direction.addThemeName("neo");
        text.setReadOnly(true);
        text.addThemeName("neo");
        answer.setMinRows(4);
        answer.addThemeName("neo");

        addFormItem(author, getTranslation("dialog_answer_author_label"));
        addFormItem(role, getTranslation("dialog_answer_role_label"));
        addFormItem(direction, getTranslation("dialog_answer_direction_label"));
        addFormItem(text, getTranslation("dialog_answer_text_label"));

        add(new H4(getTranslation("dialog_answer_text_title")));
        add(answer);
    }
}
