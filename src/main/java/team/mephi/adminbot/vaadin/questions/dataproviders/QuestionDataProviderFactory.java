package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.repository.UserQuestionRepository;

@SpringComponent
public class QuestionDataProviderFactory {
    private final QuestionService questionService;
    private final UserQuestionRepository questionRepository;
            ;
    public QuestionDataProviderFactory(QuestionService questionService, UserQuestionRepository questionRepository) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
    }

    public QuestionDataProvider createDataProvider() {
        return new QuestionDataProvider(questionService, questionRepository);
    }
}
