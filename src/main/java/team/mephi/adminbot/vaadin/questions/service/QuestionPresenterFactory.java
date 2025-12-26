package team.mephi.adminbot.vaadin.questions.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.repository.UserQuestionRepository;
import team.mephi.adminbot.vaadin.questions.dataproviders.QuestionDataProvider;

@SpringComponent
public class QuestionPresenterFactory {
    private final UserQuestionRepository questionRepository;
    public QuestionPresenterFactory(UserQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public QuestionDataProvider createDataProvider() {
        return new QuestionDataProvider(questionRepository);
    }
}
