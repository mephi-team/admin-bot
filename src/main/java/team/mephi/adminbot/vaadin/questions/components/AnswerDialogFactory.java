package team.mephi.adminbot.vaadin.questions.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * Фабрика для создания диалогов ответа на вопрос.
 */
@SpringComponent
public class AnswerDialogFactory {

    /**
     * Конструктор фабрики диалогов ответа на вопрос.
     */
    public AnswerDialogFactory() {
    }

    /**
     * Создает новый диалог для ответа на вопрос.
     *
     * @return новый экземпляр AnswerDialog
     */
    public AnswerDialog create() {
        return new AnswerDialog();
    }
}
