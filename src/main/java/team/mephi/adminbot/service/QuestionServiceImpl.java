package team.mephi.adminbot.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.model.UserAnswer;
import team.mephi.adminbot.model.UserQuestion;
import team.mephi.adminbot.model.enums.AnswerStatus;
import team.mephi.adminbot.repository.UserAnswerRepository;
import team.mephi.adminbot.repository.UserQuestionRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final AuthenticationContext authContext;
    private final UserRepository userRepository;
    private final UserQuestionRepository questionRepository;
    private final UserAnswerRepository answerRepository;

    public QuestionServiceImpl(AuthenticationContext authContext, UserRepository userRepository, UserQuestionRepository questionRepository, UserAnswerRepository answerRepository) {
        this.authContext = authContext;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
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

    @Override
    public Stream<SimpleQuestion> findAllByText(String name, Pageable pageable) {
        return questionRepository.findAllByText(name, pageable)
                .stream()
                .map(this::mapToSimple);
    }

    @Override
    public Integer countByText(String name) {
        return questionRepository.countByText(name);
    }

    @Override
    public Optional<SimpleQuestion> findByIdWithDeps(Long id) {
        return questionRepository.findByIdWithDeps(id)
                .map(this::mapToSimple);
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        questionRepository.deleteAllById(ids);
    }

    private SimpleQuestion mapToSimple(UserQuestion userQuestion) {
        return SimpleQuestion.builder()
                .id(userQuestion.getId())
                .text(userQuestion.getText())
                .date(userQuestion.getCreatedAt())
                .authorId(userQuestion.getUser().getId())
                .author(userQuestion.getUser().getUserName())
                .role(userQuestion.getRole())
                .direction(userQuestion.getDirection().getName())
                .answer(userQuestion.getAnswers().isEmpty() ? "" : userQuestion.getAnswers().stream().sorted(Comparator.comparingLong(UserAnswer::getId)).toList().getLast().getAnswerText())
                .build();
    }
}
