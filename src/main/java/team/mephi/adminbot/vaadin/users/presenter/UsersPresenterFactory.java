package team.mephi.adminbot.vaadin.users.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.CRUDViewCallback;
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

    private UserDataProvider createDataProvider(String role) {
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

    public CRUDPresenter<?> createPresenter(String role, CRUDViewCallback<?> view) {
        UserDataProvider dataProvider = createDataProvider(role);

        return switch (role) {
            case "tutor" -> new TutorPresenter(dataProvider, (TutorViewCallback) view);
            case "visitor" -> new GuestPresenter(dataProvider, (BlockingViewCallback) view);
            case "student", "free_listener" -> new StudentPresenter(dataProvider, (StudentViewCallback) view);
            // candidate, middle_candidate, lc_expert
            default -> new UsersPresenter(dataProvider, (UserViewCallback) view);
        };
    }
}
