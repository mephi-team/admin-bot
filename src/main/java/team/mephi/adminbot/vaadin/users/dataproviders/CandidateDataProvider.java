package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.UserRepository;

public class CandidateDataProvider extends BaseUserDataProvider {

    public CandidateDataProvider(UserRepository userRepository, RoleRepository roleRepository) {
        super(userRepository, roleRepository);
    }

    @Override
    protected String getRole() {
        return "candidate";
    }
}