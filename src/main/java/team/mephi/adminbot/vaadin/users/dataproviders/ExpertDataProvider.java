package team.mephi.adminbot.vaadin.users.dataproviders;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.ExpertService;
import team.mephi.adminbot.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.LC_EXPERT;

public class ExpertDataProvider extends BaseUserDataProvider {

    private final ExpertService expertService;

    private ConfigurableFilterDataProvider<SimpleUser, Void, String> provider;

    public ExpertDataProvider(UserService userService, ExpertService expertService) {
        super(userService);
        this.expertService = expertService;
    }

    @Override
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
                                sort.isUnsorted() ? Sort.by("createdAt").descending() : sort
                        );
                        return expertService.findAllByRoleAndName(getRole(), query.getFilter().orElse(""), pageable);
                    },
                    query -> expertService.countByRoleAndName(getRole(), query.getFilter().orElse("")),
                    SimpleUser::getId
            );
            provider = base.withConfigurableFilter();
        }
        return provider;
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        expertService.deleteAllById(ids);
    }

    @Override
    protected String getRole() {
        return LC_EXPERT.name();
    }
}