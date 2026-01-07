package team.mephi.adminbot.vaadin.users.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.TutorService;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.CRUDViewCallback;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.users.dataproviders.*;

@SpringComponent
public class UsersPresenterFactory {
    private final TutorService tutorService;
    private final UserService userService;
    private final NotificationService notificationService;

    public UsersPresenterFactory(TutorService tutorService, UserService userService, NotificationService notificationService) {
        this.tutorService = tutorService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    private UserDataProvider createDataProvider(String role) {
        return switch (role) {
            case "candidate" -> new CandidateDataProvider(userService);
            case "lc_expert" -> new ExpertDataProvider(userService);
            case "free_listener" -> new FreeListenerDataProvider(userService);
            case "visitor" -> new GuestsDataProvider(userService);
            case "middle_candidate" -> new MiddleCandidateDataProvider(userService);
            case "student" -> new StudentDataProvider(userService);
            case "tutor" -> new TutorDataProvider(tutorService);
            default -> throw new IllegalArgumentException("Unknown provider: " + role);
        };
    }

    public CRUDPresenter<?> createPresenter(String role, CRUDViewCallback<?> view) {
        UserDataProvider dataProvider = createDataProvider(role);

        return switch (role) {
            case "tutor" -> new TutorPresenter(dataProvider, (TutorViewCallback) view, notificationService);
            case "visitor" -> new BlockingPresenter(dataProvider, (BlockingViewCallback) view, notificationService);
            case "student", "free_listener" -> new StudentPresenter(dataProvider, (StudentViewCallback) view, notificationService);
            // candidate, middle_candidate, lc_expert
            default -> new UsersPresenter(dataProvider, (UserViewCallback) view, notificationService);
        };
    }
}
