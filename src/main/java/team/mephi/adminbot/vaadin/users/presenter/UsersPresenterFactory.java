package team.mephi.adminbot.vaadin.users.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.service.TutorService;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.users.dataproviders.*;

@SpringComponent
public class UsersPresenterFactory {
    private final TutorService tutorService;
    private final UserService userService;
    private final DialogService<?> dialogService;
    private final NotificationService notificationService;

    public UsersPresenterFactory(TutorService tutorService, UserService userService, DialogService<SimpleUser> dialogService, NotificationService notificationService) {
        this.tutorService = tutorService;
        this.userService = userService;
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    private CRUDDataProvider<?> createDataProvider(String role) {
        return switch (role) {
            case "candidate" -> new CandidateDataProvider(userService);
            case "lc_expert" -> new ExpertDataProvider(userService);
            case "free_listener" -> new FreeListenerDataProvider(userService);
            case "visitor" -> new GuestsDataProvider(userService);
            case "middle_candidate" -> new MiddleCandidateDataProvider(userService);
            case "student" -> new StudentDataProvider(userService);
            case "tutor" -> new TutorDataProviderImpl(tutorService);
            default -> throw new IllegalArgumentException("Unknown provider: " + role);
        };
    }

    public CRUDPresenter<?> createPresenter(String role) {
        CRUDDataProvider<?> dataProvider = createDataProvider(role);

        return switch (role) {
            case "tutor" -> new TutorPresenter((TutorDataProvider) dataProvider, (DialogService< SimpleTutor>) dialogService, notificationService);
            case "visitor" -> new BlockingPresenter((UserDataProvider) dataProvider, (DialogService<SimpleUser>) dialogService, notificationService);
            case "student", "free_listener" -> new StudentPresenter((UserDataProvider) dataProvider, (DialogService<SimpleUser>) dialogService, notificationService);
            // candidate, middle_candidate, lc_expert
            default -> new UsersPresenter((UserDataProvider) dataProvider, (DialogService<SimpleUser>) dialogService, notificationService);
        };
    }
}
