package team.mephi.adminbot.vaadin.mailings.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.service.TemplateService;
import team.mephi.adminbot.vaadin.CRUDDataProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TemplateDataProvider implements CRUDDataProvider<SimpleTemplate> {
    private final TemplateService templateService;
    private ConfigurableFilterDataProvider<SimpleTemplate, Void, String> provider;

    public TemplateDataProvider(TemplateService templateService) {
        this.templateService = templateService;
    }

    public ConfigurableFilterDataProvider<SimpleTemplate, Void, String> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<SimpleTemplate, String>(
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
                        return templateService.findAllByName(query.getFilter().orElse(""), pageable);
                    },
                    query -> templateService.countByName(query.getFilter().orElse("")),
                    SimpleTemplate::getId
            ).withConfigurableFilter();
        }

        return provider;
    }

    @Override
    public DataProvider<SimpleTemplate, ?> getDataProvider() {
        return getFilterableProvider();
    }

    @Override
    public Optional<SimpleTemplate> findById(Long id) {
        return templateService.findById(id);
    }

    @Override
    public SimpleTemplate save(SimpleTemplate template) {
        return templateService.save(template);
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        templateService.deleteAllById(ids);
    }
}
