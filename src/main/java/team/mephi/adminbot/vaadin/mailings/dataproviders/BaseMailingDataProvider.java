package team.mephi.adminbot.vaadin.mailings.dataproviders;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import team.mephi.adminbot.dto.MailingList;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.model.Mailing;
import team.mephi.adminbot.model.enums.Channels;
import team.mephi.adminbot.model.enums.MailingStatus;
import team.mephi.adminbot.model.objects.Filters;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseMailingDataProvider implements MailingDataProvider<SimpleMailing> {
    private final MailingRepository mailingRepository;
    private final UserRepository userRepository;
    private ConfigurableFilterDataProvider<MailingList, Void, String> provider;

    public BaseMailingDataProvider(MailingRepository mailingRepository, UserRepository userRepository) {
        this.mailingRepository = mailingRepository;
        this.userRepository = userRepository;
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
                                        .cohort(m.getFilters() != null ? m.getFilters().getCohort() : "")
                                        .direction(m.getFilters() != null ? m.getFilters().getDirection() : "")
                                        .curator(m.getFilters() != null ? m.getFilters().getCurator() : "")
                                        .city(m.getFilters() != null ? m.getFilters().getCity() : "")
                                        .text(m.getDescription())
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
    public Optional<SimpleMailing> findById(Long id) {
        return mailingRepository.findById(id).map(t -> new SimpleMailing(t.getId(), t.getName(), t.getDescription(), t.getStatus().name(), t.getCreatedBy().getId(), t.getChannels().stream().map(Enum::name).collect(Collectors.toSet()), t.getFilters().getUsers(), t.getFilters().getCohort(), t.getFilters().getDirection(), t.getFilters().getCity()));
    }

    @Override
    public SimpleMailing save(SimpleMailing mailing) {
        var result = mailing.getId() != null
                ? mailingRepository.findById(mailing.getId()).orElse(new Mailing())
                : new Mailing();
        var user = userRepository.findById(mailing.getUserId()).orElseThrow();
        result.setName(mailing.getName());
        result.setDescription(mailing.getText());
        result.setCreatedBy(user);
        result.setFilters(Filters.builder().direction(mailing.getDirection()).curator(user.getUserName()).city(mailing.getCity()).users(mailing.getUsers()).cohort(mailing.getCohort()).build());
        if (mailing.getStatus() == null) {
            result.setStatus(MailingStatus.DRAFT);
        } else {
            result.setStatus(MailingStatus.valueOf(mailing.getStatus()));
        }
        result.setChannels(mailing.getChannels().stream().map(Channels::valueOf).toList());
        mailingRepository.save(result);
        return new SimpleMailing(result.getId(), result.getName(), result.getDescription(), result.getStatus().name(), result.getCreatedBy().getId(), result.getChannels().stream().map(Enum::name).collect(Collectors.toSet()), result.getFilters().getUsers(), result.getFilters().getCohort(), result.getFilters().getDirection(), result.getFilters().getCity());
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        mailingRepository.deleteAllById(ids);
    }

    protected abstract List<MailingStatus> getStatuses();
}
