package team.mephi.adminbot.vaadin.mailings.dataproviders;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.stereotype.Component;
import team.mephi.adminbot.dto.MailingList;
import team.mephi.adminbot.repository.MailingRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component("sent")
public class MailingDataProvider {
    private final MailingRepository mailingRepository;
    private ConfigurableFilterDataProvider<MailingList, Void, String> provider;

    public MailingDataProvider(MailingRepository mailingRepository) {
        this.mailingRepository = mailingRepository;
    }

    public ConfigurableFilterDataProvider<MailingList, Void, String> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<MailingList, String>(
                    query -> {
                        return mailingRepository.findMailingByName(query.getFilter().orElse(""))
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
                    query -> mailingRepository.countByName(query.getFilter().orElse("")),
                    MailingList::getId
            ).withConfigurableFilter();
        }

        return provider;
    }

    public DataProvider<MailingList, ?> getDataProvider() {
        return getFilterableProvider();
    }

    public void deleteAllById(Iterable<Long> ids) {
        mailingRepository.deleteAllById(ids);
    }

    public void refresh() {
        if (provider != null) {
            provider.refreshAll();
        }
    }
}
