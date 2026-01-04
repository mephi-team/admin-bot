package team.mephi.adminbot.vaadin.mailings.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.vaadin.CRUDDataProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseMailingDataProvider implements CRUDDataProvider<SimpleMailing> {
    private final MailingService mailingService;
    private final MailingRepository mailingRepository;
    private ConfigurableFilterDataProvider<SimpleMailing, Void, String> provider;

    public BaseMailingDataProvider(MailingService mailingService, MailingRepository mailingRepository) {
        this.mailingService = mailingService;
        this.mailingRepository = mailingRepository;
    }

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
                        return mailingRepository.findMailingByName(query.getFilter().orElse(""), getStatuses().stream().map(Enum::name).toList(), pageable)
                                .stream()
                                .map(m -> SimpleMailing.builder()
                                        .id(m.getId())
                                        .date(m.getCreatedAt())
                                        .users(m.getFilters() != null ? m.getFilters().getUsers() : "")
                                        .cohort(m.getFilters() != null ? m.getFilters().getCohort() : "")
                                        .direction(m.getFilters() != null ? m.getFilters().getDirection() : "")
                                        .curator(m.getFilters() != null ? m.getFilters().getCurator() : "")
                                        .city(m.getFilters() != null ? m.getFilters().getCity() : "")
                                        .channels(m.getChannels().stream().map(Enum::name).collect(Collectors.toSet()))
                                        .text(m.getDescription())
                                        .status(m.getStatus().name())
                                        .build());
                    },
                    query -> mailingRepository.countByName(query.getFilter().orElse(""), getStatuses()),
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
        return mailingRepository.findById(id).map(t -> SimpleMailing.builder()
                .id(t.getId())
                .date(t.getCreatedAt())
                .status(t.getStatus().name())
                .build());
    }

    @Override
    public SimpleMailing save(SimpleMailing mailing) {
        return mailingService.save(mailing);
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        mailingRepository.deleteAllById(ids);
    }

    protected abstract List<MailingStatus> getStatuses();
}
