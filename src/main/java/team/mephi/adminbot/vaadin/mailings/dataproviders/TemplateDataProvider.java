package team.mephi.adminbot.vaadin.mailings.dataproviders;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.stereotype.Component;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.dto.TemplateListDto;
import team.mephi.adminbot.repository.MailTemplateRepository;

import java.util.Optional;
import java.util.Set;

@Component("templates")
public class TemplateDataProvider implements MailingDataProvider<SimpleMailing> {
    private final MailTemplateRepository mailTemplateRepository;
    private ConfigurableFilterDataProvider<TemplateListDto, Void, String> provider;

    public TemplateDataProvider(MailTemplateRepository mailTemplateRepository) {
        this.mailTemplateRepository = mailTemplateRepository;
    }

    public ConfigurableFilterDataProvider<TemplateListDto, Void, String> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<TemplateListDto, String>(
                    query -> {
                        return mailTemplateRepository.findAllByName(query.getFilter().orElse(""))
                                .stream()
                                .map(m -> TemplateListDto.builder()
                                        .id(m.getId())
                                        .name(m.getName())
                                        .text(m.getBodyText())
                                        .build())
                                .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                                .limit(query.getLimit()); // Берем только нужное количество
                    },
                    query -> mailTemplateRepository.countByName(query.getFilter().orElse("")),
                    TemplateListDto::getId
            ).withConfigurableFilter();
        }

        return provider;
    }

    @Override
    public DataProvider<TemplateListDto, ?> getDataProvider() {
        return getFilterableProvider();
    }

    @Override
    public Optional<SimpleMailing> findById(Long id) {
        return mailTemplateRepository.findById(id).map(t -> new SimpleMailing(t.getId(),t.getName(), t.getBodyText(), t.getCreatedBy().getId(), Set.of()));
    }

    @Override
    public SimpleMailing save(SimpleMailing user) {
        return null;
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        mailTemplateRepository.deleteAllById(ids);
    }

    @Override
    public void refresh() {
        if (provider != null) {
            provider.refreshAll();
        }
    }
}
