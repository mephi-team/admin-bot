package team.mephi.adminbot.vaadin.mailings.dataproviders;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import team.mephi.adminbot.dto.MailingList;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.repository.MailingRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public abstract class BaseMailingDataProvider implements MailingDataProvider {
    private final MailingRepository mailingRepository;
    private ConfigurableFilterDataProvider<MailingList, Void, String> provider;

    public BaseMailingDataProvider(MailingRepository mailingRepository) {
        this.mailingRepository = mailingRepository;
    }

    public ConfigurableFilterDataProvider<MailingList, Void, String> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<MailingList, String>(
                    query -> {
                        return mailingRepository.findMailingByName(query.getFilter().orElse(""), getStatuses())
                                .stream()
                                .map(m -> MailingList.builder()
                                        .id(m.getId())
                                        .name(m.getName())
                                        .date(LocalDateTime.ofInstant(m.getCreatedAt(), ZoneId.of("UTC")))
                                        .users(m.getFilters() != null ? m.getFilters().getUsers() : "")
                                        .cohort(m.getFilters() != null ? m.getFilters().getCurator() : "")
                                        .direction(m.getFilters() != null ? m.getFilters().getDirection() : "")
                                        .curator(m.getFilters() != null ? m.getFilters().getCurator() : "")
                                        .city(m.getFilters() != null ? m.getFilters().getCity() : "")
                                        .status(m.getStatus().name())
                                        .build())
                                .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                                .limit(query.getLimit()); // Берем только нужное количество
                    },
                    query -> mailingRepository.countByName(query.getFilter().orElse(""), getStatuses()),
                    MailingList::getId
            ).withConfigurableFilter();
        }

        return provider;
    }

    @Override
    public DataProvider<MailingList, ?> getDataProvider() {
        return getFilterableProvider();
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        mailingRepository.deleteAllById(ids);
    }

    @Override
    public void refresh() {
        if (provider != null) {
            provider.refreshAll();
        }
    }

    protected abstract List<MailingStatus> getStatuses();
}
