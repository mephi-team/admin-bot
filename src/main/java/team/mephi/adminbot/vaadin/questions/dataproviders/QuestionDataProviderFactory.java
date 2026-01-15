package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.QuestionService;

/**
 * Фабрика для создания провайдеров данных вопросов.
 */
@SpringComponent
public class QuestionDataProviderFactory {
    private final QuestionService questionService;

    /**
     * Конструктор фабрики провайдеров данных вопросов.
     *
     * @param questionService сервис для работы с вопросами
     */
    public QuestionDataProviderFactory(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Создает новый провайдер данных для вопросов.
     *
     * @return новый экземпляр QuestionDataProvider
     */
    public QuestionDataProvider createDataProvider() {
        return new QuestionDataProvider(questionService);
    }
}
