package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.repository.UserAnswerRepository;
import team.mephi.adminbot.repository.UserQuestionRepository;
import team.mephi.adminbot.repository.UserRepository;

@SpringComponent
public class QuestionDataProviderFactory {
    private final UserQuestionRepository questionRepository;
    private final UserAnswerRepository answerRepository;
    private final UserRepository userRepository
            ;
    public QuestionDataProviderFactory(UserQuestionRepository questionRepository, UserAnswerRepository answerRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public QuestionDataProvider createDataProvider() {
        return new QuestionDataProvider(questionRepository, answerRepository, userRepository);
    }
}
