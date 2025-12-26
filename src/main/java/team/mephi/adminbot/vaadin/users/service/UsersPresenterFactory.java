package team.mephi.adminbot.vaadin.users.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.users.dataproviders.*;

@SpringComponent
public class UsersPresenterFactory {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TutorRepository tutorRepository;

    public UsersPresenterFactory(UserRepository userRepository, RoleRepository roleRepository, TutorRepository tutorRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tutorRepository = tutorRepository;
    }

    public UserDataProvider createDataProvider(String role) {
        return switch (role) {
            case "candidate" -> new CandidateDataProvider(userRepository, roleRepository);
            case "lc_expert" -> new ExpertDataProvider(userRepository, roleRepository);
            case "free_listener" -> new FreeListenerDataProvider(userRepository, roleRepository);
            case "visitor" -> new GuestsDataProvider(userRepository, roleRepository);
            case "middle_candidate" -> new MiddleCandidateDataProvider(userRepository, roleRepository);
            case "student" -> new StudentDataProvider(userRepository, roleRepository);
            case "tutor" -> new TutorDataProvider(tutorRepository);
            default -> throw new IllegalArgumentException("Unknown provider: " + role);
        };
    }
}
