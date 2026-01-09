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
import team.mephi.adminbot.vaadin.users.tabs.UserTabType;

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

    private CRUDDataProvider<?> createDataProvider(UserTabType role) {
        return switch (role) {
            case CANDIDATE -> new CandidateDataProvider(userService);
            case LC_EXPERT -> new ExpertDataProvider(userService);
            case FREE_LISTENER -> new FreeListenerDataProvider(userService);
            case VISITOR -> new GuestsDataProvider(userService);
            case MIDDLE_CANDIDATE -> new MiddleCandidateDataProvider(userService);
            case STUDENT -> new StudentDataProvider(userService);
            case TUTOR -> new TutorDataProviderImpl(tutorService);
        };
    }

    public CRUDPresenter<?> createPresenter(UserTabType role) {
        CRUDDataProvider<?> dataProvider = createDataProvider(role);

        return switch (role) {
            case TUTOR -> new TutorPresenter((TutorDataProvider) dataProvider, (DialogService< SimpleTutor>) dialogService, notificationService);
            case VISITOR -> new BlockingPresenter((UserDataProvider) dataProvider, (DialogService<SimpleUser>) dialogService, notificationService);
            case STUDENT, FREE_LISTENER -> new StudentPresenter((UserDataProvider) dataProvider, (DialogService<SimpleUser>) dialogService, notificationService);
            // candidate, middle_candidate, lc_expert
            default -> new UsersPresenter((UserDataProvider) dataProvider, (DialogService<SimpleUser>) dialogService, notificationService);
        };
    }
}
