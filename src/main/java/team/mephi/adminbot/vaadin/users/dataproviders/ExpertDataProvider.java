package team.mephi.adminbot.vaadin.users.dataproviders;

import org.springframework.stereotype.Component;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;

@Component("lc_expert")
public class ExpertDataProvider extends BaseUserDataProvider {

    public ExpertDataProvider(UserRepository userRepository, RoleRepository roleRepository) {
        super(userRepository, roleRepository);
    }

    @Override
    protected String getRole() {
        return "lc_expert";
    }
}