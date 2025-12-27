package team.mephi.adminbot.vaadin.questions.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.repository.UserAnswerRepository;
import team.mephi.adminbot.repository.UserQuestionRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.questions.dataproviders.QuestionDataProvider;

@SpringComponent
public class QuestionPresenterFactory {
    private final UserQuestionRepository questionRepository;
    private final UserAnswerRepository answerRepository;
    private final UserRepository userRepository
            ;
    public QuestionPresenterFactory(UserQuestionRepository questionRepository, UserAnswerRepository answerRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public QuestionDataProvider createDataProvider() {
        return new QuestionDataProvider(questionRepository, answerRepository, userRepository);
    }
}
