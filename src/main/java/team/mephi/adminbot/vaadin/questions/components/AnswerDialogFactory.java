package team.mephi.adminbot.vaadin.questions.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * Фабрика для создания диалогов ответа на вопрос.
 */
@SpringComponent
public class AnswerDialogFactory {
    public AnswerDialogFactory() {

    }

    public AnswerDialog create() {
        return new AnswerDialog();
    }
}
