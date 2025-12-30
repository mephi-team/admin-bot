package team.mephi.adminbot.vaadin.users.dataproviders;

import com.vaadin.flow.data.provider.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.users.presenter.UserDataProvider;
import team.mephi.adminbot.vaadin.users.service.UserCountService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseUserDataProvider implements UserDataProvider {
    private final UserCountService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private ConfigurableFilterDataProvider<SimpleUser, Void, String> provider;

    public BaseUserDataProvider(UserRepository userRepository, RoleRepository roleRepository, UserCountService userService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
                                sort.isUnsorted() ? Sort.by("createdAt").descending() : sort
                        );
                        return userRepository.findAllByRoleAndName(getRole(), query.getFilter().orElse(""), pageable)
                            .stream()
                            .map(u -> SimpleUser.builder()
                                    .id(u.getId())
                                    .role(u.getRole().getName())
                                    .firstName(u.getFirstName())
                                    .lastName(u.getLastName())
                                    .fullName(u.getUserName())
                                    .phoneNumber(u.getPhoneNumber())
                                    .tgId(u.getTgId())
                                    .tgName(u.getTgName())
                                    .email(u.getEmail())
                                    .phoneNumber(u.getPhoneNumber())
                                    .pdConsent(u.getPdConsent())
                                    .status(u.getStatus().name())
                                    .build());
                        },
                    query -> userRepository.countByRoleAndName(getRole(), query.getFilter().orElse("")),
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
        return userService.findById(id);
    }

    @Override
    @Transactional
    public SimpleUser save(SimpleUser dto) {
        return userService.save(dto);
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public void blockAllById(Iterable<Long> ids) {
        userRepository.blockAllById(ids);
    }

    protected abstract String getRole();
}