package team.mephi.adminbot.vaadin.users.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.TutorService;
import team.mephi.adminbot.vaadin.users.presenter.UserDataProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TutorDataProvider implements UserDataProvider {

    private final TutorService tutorService;

    private ConfigurableFilterDataProvider<SimpleUser, Void, String> provider;

    public TutorDataProvider(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    public ConfigurableFilterDataProvider<SimpleUser, Void, String> getFilterableProvider() {
        if (provider == null) {
            CallbackDataProvider<SimpleUser, String> base = new CallbackDataProvider<>(
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
                        return tutorService.findAllByName(query.getFilter().orElse(""), pageable);
                        },
                    query -> tutorService.countByName(query.getFilter().orElse("")),
                    SimpleUser::getId
            );
            provider = base.withConfigurableFilter();
        }
        return provider;
    }

    @Override
    public DataProvider<SimpleUser, ?> getDataProvider() {
        return getFilterableProvider();
    }

    @Override
    public Optional<SimpleUser> findById(Long id) {
        return tutorService.findById(id);
    }

    @Override
    public SimpleUser save(SimpleUser dto) {
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