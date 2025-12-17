package team.mephi.adminbot.vaadin.users.dataproviders;

import org.springframework.stereotype.Component;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;

@Component("student")
public class StudentDataProvider extends BaseUserDataProvider {

    public StudentDataProvider(UserRepository userRepository, RoleRepository roleRepository) {
        super(userRepository, roleRepository);
    }

    @Override
    protected String getRole() {
        return "student";
    }
}