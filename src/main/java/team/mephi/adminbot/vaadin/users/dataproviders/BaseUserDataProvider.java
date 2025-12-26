package team.mephi.adminbot.vaadin.users.dataproviders;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.Optional;

public abstract class BaseUserDataProvider implements UserDataProvider {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private ConfigurableFilterDataProvider<UserDto, Void, String> provider;

    public BaseUserDataProvider(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public ConfigurableFilterDataProvider<UserDto, Void, String> getFilterableProvider() {
        if (provider == null) {
            CallbackDataProvider<UserDto, String> base = new CallbackDataProvider<>(
                    query -> userRepository.findAllByRoleAndName(getRole(), query.getFilter().orElse(""))
                            .stream()
                            .map(u -> new UserDto(
                                    u.getId(),
                                    u.getUserName(),
                                    u.getLastName() + " " + u.getFirstName(),
                                    u.getEmail(),
                                    u.getTgName(),
                                    u.getPhoneNumber(),
                                    u.getPdConsent(),
                                    u.getCohort(),
                                    u.getDirection() != null ? u.getDirection().getName() : "",
                                    u.getCity(),
                                    u.getStatus() != null ? u.getStatus().name() : "",
                                    u.getDeleted()
                            ))
                            .skip(query.getOffset())
                            .limit(query.getLimit()),
                    query -> userRepository.countByRoleAndName(getRole(), query.getFilter().orElse("")),
                    UserDto::getId
            );
            provider = base.withConfigurableFilter();
        }
        return provider;
    }

    @Override
    public DataProvider<UserDto, ?> getDataProvider() {
        return getFilterableProvider();
    }

    @Override
    public Optional<SimpleUser> findById(Long id) {
        return userRepository.findSimpleUserById(id);
    }

    @Override
    public SimpleUser save(SimpleUser dto) {
        User user = dto.getId() != null
                ? userRepository.findById(dto.getId()).orElse(new User())
                : new User();
        user.setRole(roleRepository.findByCode(dto.getRole()).orElseThrow());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setTgId(dto.getTelegram());
        user = userRepository.save(user);
        return new SimpleUser(user.getId(), user.getRole().getCode(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getTgId());
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