package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.QuestionService;

/**
 * Фабрика для создания провайдеров данных вопросов.
 */
@SpringComponent
public class QuestionDataProviderFactory {
    private final QuestionService questionService;

    public QuestionDataProviderFactory(QuestionService questionService) {
        this.questionService = questionService;
    }

    public QuestionDataProvider createDataProvider() {
        return new QuestionDataProvider(questionService);
    }
}
