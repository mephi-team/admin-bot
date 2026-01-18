package team.mephi.adminbot.vaadin.users.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.service.TutorService;
import team.mephi.adminbot.vaadin.users.presenter.TutorDataProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Провайдер данных для пользователей с ролью "Куратор".
 * Предоставляет функциональность фильтрации и пагинации.
 */
public class TutorDataProviderImpl implements TutorDataProvider {

    private final TutorService tutorService;

    private ConfigurableFilterDataProvider<SimpleTutor, Void, String> provider;

    /**
     * Конструктор провайдера данных для кураторов.
     *
     * @param tutorService сервис для работы с кураторами.
     */
    public TutorDataProviderImpl(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    /**
     * Получает провайдер данных с возможностью фильтрации по имени куратора.
     *
     * @return провайдер данных с фильтрацией.
     */
    public ConfigurableFilterDataProvider<SimpleTutor, Void, String> getFilterableProvider() {
        if (provider == null) {
            CallbackDataProvider<SimpleTutor, String> base = new CallbackDataProvider<>(
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
                                sort.isUnsorted() ? Sort.by("id").descending() : sort
                        );
                        return tutorService.findAllWithDirectionsAndStudents(query.getFilter().orElse(""), pageable);
                    },
                    query -> tutorService.countByName(query.getFilter().orElse("")),
                    SimpleTutor::getId
            );
            provider = base.withConfigurableFilter();
        }
        return provider;
    }

    @Override
    public DataProvider<SimpleTutor, ?> getDataProvider() {
        return getFilterableProvider();
    }

    @Override
    public Optional<SimpleTutor> findById(Long id) {
        return tutorService.findById(id);
    }

    @Override
    public SimpleTutor save(SimpleTutor dto) {
        return tutorService.save(dto);
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        tutorService.deleteAllById(ids);
    }

    @Override
    public void blockAllById(Iterable<Long> ids) {
        tutorService.blockAllById(ids);
    }
}