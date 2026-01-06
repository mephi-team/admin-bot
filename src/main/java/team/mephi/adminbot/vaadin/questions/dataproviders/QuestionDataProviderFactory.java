package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;

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
