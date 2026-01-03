package team.mephi.adminbot.vaadin.questions.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.model.UserAnswer;
import team.mephi.adminbot.model.UserQuestion;
import team.mephi.adminbot.model.enums.AnswerStatus;
import team.mephi.adminbot.repository.UserAnswerRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.questions.dataproviders.QuestionService;

import java.time.Instant;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final UserRepository userRepository;
    private final UserAnswerRepository answerRepository;

    public QuestionServiceImpl(UserRepository userRepository, UserAnswerRepository answerRepository) {
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    @Override
    public void saveAnswer(Long question, String user, String text) {
        var answer = UserAnswer.builder()
                .status(AnswerStatus.SENT)
                .answeredAt(Instant.now())
                .answeredBy(userRepository.findByEmail(user).orElseThrow())
                .question(UserQuestion.builder().id(question).build())
                .answerText(text)
                .build();
        answerRepository.save(answer);
    }
}
