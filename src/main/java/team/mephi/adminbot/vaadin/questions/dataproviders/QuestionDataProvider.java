package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.model.UserAnswer;
import team.mephi.adminbot.repository.UserQuestionRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuestionDataProvider {
    private final QuestionService questionService;
    private final UserQuestionRepository questionRepository;
    private ConfigurableFilterDataProvider<SimpleQuestion, Void, String> provider;

    public QuestionDataProvider(QuestionService questionService, UserQuestionRepository questionRepository) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
    }

    public ConfigurableFilterDataProvider<SimpleQuestion, Void, String> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<SimpleQuestion, String>(
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
                                .map(u -> SimpleQuestion.builder()
                                        .id(u.getId())
                                        .text(u.getText())
                                        .date(u.getCreatedAt())
                                        .authorId(u.getUser().getId())
                                        .author(u.getUser().getUserName())
                                        .role(u.getRole())
                                        .direction(u.getDirection().getName())
                                        .answer(u.getAnswers().isEmpty() ? "" : u.getAnswers().stream().sorted(Comparator.comparingLong(UserAnswer::getId)).toList().getLast().getAnswerText())
                                        .build());
                        },
                    query -> questionRepository.countByText(query.getFilter().orElse("")),
                    SimpleQuestion::getId
            ).withConfigurableFilter();
        }

        return provider;
    }

    public DataProvider<SimpleQuestion, ?> getDataProvider() {
        return getFilterableProvider();
    }

    public Optional<SimpleQuestion> findById(Long id) {
        return questionRepository.findByIdWithDeps(id).map(t -> SimpleQuestion.builder().build());
    }

    public void deleteAllById(Iterable<Long> ids) {
        questionRepository.deleteAllById(ids);
    }

    public SimpleQuestion saveAnswer(SimpleQuestion question) {
        return questionService.saveAnswer(question);
    }
}
