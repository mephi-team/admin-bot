package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleQuestion;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuestionDataProvider {
    private final QuestionService questionService;
    private ConfigurableFilterDataProvider<SimpleQuestion, Void, String> provider;

    public QuestionDataProvider(QuestionService questionService) {
        this.questionService = questionService;
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
                        return questionService.findAllByText(query.getFilter().orElse(""), pageable);
                        },
                    query -> questionService.countByText(query.getFilter().orElse("")),
                    SimpleQuestion::getId
            ).withConfigurableFilter();
        }

        return provider;
    }

    public DataProvider<SimpleQuestion, ?> getDataProvider() {
        return getFilterableProvider();
    }

    public Optional<SimpleQuestion> findById(Long id) {
        return questionService.findByIdWithDeps(id);
    }

    public void deleteAllById(Iterable<Long> ids) {
        questionService.deleteAllById(ids);
    }

    public SimpleQuestion saveAnswer(SimpleQuestion question) {
        return questionService.saveAnswer(question);
    }
}
