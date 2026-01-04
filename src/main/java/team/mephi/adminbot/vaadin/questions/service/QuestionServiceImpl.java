package team.mephi.adminbot.vaadin.questions.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.model.UserAnswer;
import team.mephi.adminbot.model.UserQuestion;
import team.mephi.adminbot.model.enums.AnswerStatus;
import team.mephi.adminbot.repository.UserAnswerRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.questions.dataproviders.QuestionService;

import java.time.Instant;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final AuthenticationContext authContext;
    private final UserRepository userRepository;
    private final UserAnswerRepository answerRepository;

    public QuestionServiceImpl(AuthenticationContext authContext, UserRepository userRepository, UserAnswerRepository answerRepository) {
        this.authContext = authContext;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    @Override
    public SimpleQuestion saveAnswer(SimpleQuestion question) {
        var user = authContext.getAuthenticatedUser(DefaultOidcUser.class).orElseThrow();

        var answer = UserAnswer.builder()
                .status(AnswerStatus.SENT)
                .answeredAt(Instant.now())
                .answeredBy(userRepository.findByEmail(user.getUserInfo().getEmail()).orElseThrow())
                .question(UserQuestion.builder().id(question.getId()).build())
                .answerText(question.getAnswer())
                .build();

        answerRepository.save(answer);

        return question;
    }
}
