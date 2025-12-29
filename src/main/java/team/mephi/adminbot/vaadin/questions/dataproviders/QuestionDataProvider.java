package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.dto.UserQuestionDto;
import team.mephi.adminbot.model.UserAnswer;
import team.mephi.adminbot.model.enums.AnswerStatus;
import team.mephi.adminbot.repository.UserAnswerRepository;
import team.mephi.adminbot.repository.UserQuestionRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuestionDataProvider {
    private final UserQuestionRepository questionRepository;
    private final UserAnswerRepository answerRepository;
    private final UserRepository userRepository;
    private ConfigurableFilterDataProvider<UserQuestionDto, Void, String> provider;

    public QuestionDataProvider(UserQuestionRepository questionRepository, UserAnswerRepository answerRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public ConfigurableFilterDataProvider<UserQuestionDto, Void, String> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<UserQuestionDto, String>(
                    query -> {
                        List<QuerySortOrder> sortOrders = query.getSortOrders();
                        Sort sort = Sort.by(sortOrders.stream()
                                .map(so -> so.getDirection() == SortDirection.ASCENDING
                                        ? Sort.Order.asc(so.getSorted())
                                        : Sort.Order.desc(so.getSorted()))
                                .collect(Collectors.toList()));
                        Pageable pageable = PageRequest.of(
                                query.getOffset() / query.getLimit(),
                                query.getLimit(),
                                sort.isUnsorted() ? Sort.by("createdAt").descending() : sort
                        );
                        return questionRepository.findAllByText(query.getFilter().orElse(""), pageable)
                                .stream()
                                .map(u -> UserQuestionDto
                                                .builder()
                                                .id(u.getId())
                                                .question(u.getText())
                                                .date(u.getCreatedAt())
                                                .user(u.getUser().getUserName())
                                                .userId(u.getUser().getId())
                                                .role(u.getRole())
                                                .direction(u.getDirection() != null ? u.getDirection().getName() : "")
                                                .answer(u.getAnswers().isEmpty() ? "" : u.getAnswers().stream().sorted(Comparator.comparingLong(UserAnswer::getId)).toList().getLast().getAnswerText())
                                                .build()
                                );
                        },
                    query -> questionRepository.countByText(query.getFilter().orElse("")),
                    UserQuestionDto::getId
            ).withConfigurableFilter();
        }

        return provider;
    }

    public DataProvider<UserQuestionDto, ?> getDataProvider() {
        return getFilterableProvider();
    }

    public Optional<SimpleQuestion> findById(Long id) {
        return questionRepository.findByIdWithDeps(id).map(t -> new SimpleQuestion(t.getId(), t.getUser().getUserName(), t.getRole(), t.getDirection().getName(), t.getText(), t.getAnswers().isEmpty() ? "" : t.getAnswers().stream().sorted(Comparator.comparingLong(UserAnswer::getId)).toList().getLast().getAnswerText()));
    }

    public void deleteAllById(Iterable<Long> ids) {
        questionRepository.deleteAllById(ids);
    }

    public void refresh() {
        if (provider != null) {
            provider.refreshAll();
        }
    }

    public void saveAnswer(Long question, String user, String text) {
        var answer = UserAnswer.builder()
                .status(answerRepository.countByQuestionId(question) == 0 ? AnswerStatus.SENT : AnswerStatus.UPDATED)
                .answeredAt(Instant.now())
                .answeredBy(userRepository.findByEmail(user).orElseThrow())
                .question(questionRepository.findByIdWithDeps(question).orElseThrow())
                .answerText(text)
                .build();
        answerRepository.save(answer);
    }
}
