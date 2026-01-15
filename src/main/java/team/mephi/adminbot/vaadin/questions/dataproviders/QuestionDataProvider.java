package team.mephi.adminbot.vaadin.questions.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.service.QuestionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Провайдер данных для сущностей SimpleQuestion с поддержкой фильтрации и сортировки.
 */
public class QuestionDataProvider {
    private final QuestionService questionService;
    private ConfigurableFilterDataProvider<SimpleQuestion, Void, String> provider;

    /**
     * Конструктор провайдера данных для вопросов.
     *
     * @param questionService сервис для работы с вопросами
     */
    public QuestionDataProvider(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Получает провайдер данных с поддержкой фильтрации по тексту вопроса.
     *
     * @return провайдер данных с фильтрацией
     */
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

    /**
     * Получает провайдер данных для использования в компонентах Vaadin.
     *
     * @return провайдер данных
     */
    public DataProvider<SimpleQuestion, ?> getDataProvider() {
        return getFilterableProvider();
    }

    /**
     * Находит вопрос по его идентификатору.
     *
     * @param id идентификатор вопроса
     * @return опциональный вопрос
     */
    public Optional<SimpleQuestion> findById(Long id) {
        return questionService.findByIdWithDeps(id);
    }

    /**
     * Удаляет вопрос по его идентификатору.
     *
     * @param ids идентификатор вопроса
     */
    public void deleteAllById(Iterable<Long> ids) {
        questionService.deleteAllById(ids);
    }

    /**
     * Сохраняет ответ на вопрос.
     *
     * @param question вопрос с ответом
     * @return сохраненный вопрос
     */
    public SimpleQuestion saveAnswer(SimpleQuestion question) {
        return questionService.saveAnswer(question);
    }
}
