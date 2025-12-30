package team.mephi.adminbot.vaadin.users.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.CRUDViewCallback;
import team.mephi.adminbot.vaadin.users.dataproviders.*;
import team.mephi.adminbot.vaadin.users.service.UserCountService;

@SpringComponent
public class UsersPresenterFactory {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TutorRepository tutorRepository;
    private final UserCountService userService;

    public UsersPresenterFactory(UserRepository userRepository, RoleRepository roleRepository, TutorRepository tutorRepository, UserCountService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tutorRepository = tutorRepository;
        this.userService = userService;
    }

    private UserDataProvider createDataProvider(String role) {
        return switch (role) {
            case "candidate" -> new CandidateDataProvider(userRepository, roleRepository, userService);
            case "lc_expert" -> new ExpertDataProvider(userRepository, roleRepository, userService);
            case "free_listener" -> new FreeListenerDataProvider(userRepository, roleRepository, userService);
            case "visitor" -> new GuestsDataProvider(userRepository, roleRepository, userService);
            case "middle_candidate" -> new MiddleCandidateDataProvider(userRepository, roleRepository, userService);
            case "student" -> new StudentDataProvider(userRepository, roleRepository, userService);
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
