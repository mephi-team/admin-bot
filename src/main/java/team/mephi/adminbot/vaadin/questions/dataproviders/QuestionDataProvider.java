package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.UserQuestionDto;
import team.mephi.adminbot.repository.UserQuestionRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDataProvider {
    private final  UserQuestionRepository questionRepository;
    private ConfigurableFilterDataProvider<UserQuestionDto, Void, String> provider;

    public QuestionDataProvider(UserQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
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
                                                .date(LocalDateTime.ofInstant(u.getCreatedAt(), ZoneId.of("UTC")))
                                                .user(u.getUser().getUserName())
                                                .role(u.getRole())
//                                    .direction(u.getDirection() != null ? u.getDirection().getName() : "")
                                                .answer(!u.getAnswers().isEmpty() ? u.getAnswers().getLast().getAnswerText() : "")
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

    public void deleteAllById(Iterable<Long> ids) {
        questionRepository.deleteAllById(ids);
    }

    public void refresh() {
        if (provider != null) {
            provider.refreshAll();
        }
    }
}
