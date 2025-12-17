package team.mephi.adminbot.vaadin.users.dataproviders;

import org.springframework.stereotype.Component;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;

@Component("visitor")
public class GuestsDataProvider extends BaseUserDataProvider {
    public GuestsDataProvider(UserRepository userRepository, RoleRepository roleRepository) {
        super(userRepository, roleRepository);
    }

    @Override
    protected String getRole() {
        return "visitor";
    }
}