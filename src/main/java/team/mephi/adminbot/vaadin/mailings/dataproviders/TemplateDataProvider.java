package team.mephi.adminbot.vaadin.mailings.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.dto.TemplateListDto;
import team.mephi.adminbot.model.MailTemplate;
import team.mephi.adminbot.repository.MailTemplateRepository;
import team.mephi.adminbot.vaadin.CRUDDataProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TemplateDataProvider implements CRUDDataProvider<SimpleTemplate> {
    private final MailTemplateRepository mailTemplateRepository;
    private ConfigurableFilterDataProvider<TemplateListDto, Void, String> provider;

    public TemplateDataProvider(MailTemplateRepository mailTemplateRepository) {
        this.mailTemplateRepository = mailTemplateRepository;
    }

    public ConfigurableFilterDataProvider<TemplateListDto, Void, String> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<TemplateListDto, String>(
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
                        return mailTemplateRepository.findAllByName(query.getFilter().orElse(""), pageable)
                                .stream()
                                .map(m -> TemplateListDto.builder()
                                        .id(m.getId())
                                        .name(m.getName())
                                        .text(m.getBodyText())
                                        .build());
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
    public Optional<SimpleTemplate> findById(Long id) {
        return mailTemplateRepository.findById(id).map(t -> new SimpleTemplate(t.getId(),t.getName(), t.getBodyText()));
    }

    @Override
    public SimpleTemplate save(SimpleTemplate template) {
        var result = template.getId() != null
                ? mailTemplateRepository.findById(template.getId()).orElse(new MailTemplate())
                : new MailTemplate();
        result.setName(template.getName());
        result.setSubject(template.getName());
        result.setBodyText(template.getText());
        mailTemplateRepository.save(result);
        return new SimpleTemplate(result.getId(), result.getName(), result.getBodyText());
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        mailTemplateRepository.deleteAllById(ids);
    }
}
