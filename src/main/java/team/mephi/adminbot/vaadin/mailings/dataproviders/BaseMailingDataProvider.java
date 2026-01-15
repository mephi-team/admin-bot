package team.mephi.adminbot.vaadin.mailings.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.service.MailingService;
import team.mephi.adminbot.vaadin.CRUDDataProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Базовый класс для провайдеров данных рассылок с поддержкой фильтрации и пагинации.
 */
public abstract class BaseMailingDataProvider implements CRUDDataProvider<SimpleMailing> {
    private final MailingService mailingService;
    private ConfigurableFilterDataProvider<SimpleMailing, Void, String> provider;

    /**
     * Конструктор базового провайдера данных рассылок.
     *
     * @param mailingService сервис для работы с рассылками
     */
    public BaseMailingDataProvider(MailingService mailingService) {
        this.mailingService = mailingService;
    }

    /**
     * Возвращает провайдер данных с поддержкой фильтрации по имени рассылки и статусам.
     *
     * @return провайдер данных с фильтрацией
     */
    public ConfigurableFilterDataProvider<SimpleMailing, Void, String> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<SimpleMailing, String>(
                    query -> {
                        List<QuerySortOrder> sortOrders = query.getSortOrders();
                        Sort sort = JpaSort.by(
                                sortOrders.stream()
                                        .map(so -> JpaSort.unsafe(
                                                so.getDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC,
                                                so.getSorted()).stream().findAny().orElseThrow()
                                        )
                                        .collect(Collectors.toList())
                        );
                        Pageable pageable = PageRequest.of(
                                query.getOffset() / query.getLimit(),
                                query.getLimit(),
                                sort.isUnsorted() ? Sort.by("created_at").descending() : sort
                        );
                        return mailingService.findMailingByName(query.getFilter().orElse(""), getStatuses().stream().map(Enum::name).toList(), pageable);
                    },
                    query -> mailingService.countByName(query.getFilter().orElse(""), getStatuses()),
                    SimpleMailing::getId
            ).withConfigurableFilter();
        }

        return provider;
    }

    @Override
    public DataProvider<SimpleMailing, ?> getDataProvider() {
        return getFilterableProvider();
    }

    @Override
    public Optional<SimpleMailing> findById(Long id) {
        return mailingService.findById(id);
    }

    @Override
    public SimpleMailing save(SimpleMailing mailing) {
        return mailingService.save(mailing);
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        mailingService.deleteAllById(ids);
    }

    /**
     * Абстрактный метод для получения списка статусов рассылок для фильтрации.
     *
     * @return список статусов рассылок
     */
    protected abstract List<MailingStatus> getStatuses();
}
