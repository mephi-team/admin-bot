package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.users.service.UserCountService;

public class FreeListenerDataProvider extends BaseUserDataProvider {

    public FreeListenerDataProvider(UserRepository userRepository, RoleRepository roleRepository, UserCountService userService) {
        super(userRepository, roleRepository, userService);
    }

    @Override
    protected String getRole() {
        return "free_listener";
    }
}