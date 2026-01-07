package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.StudentActions;

import java.util.List;

public class StudentPresenter extends UsersPresenter implements StudentActions {
    private final StudentViewCallback view;
    private final NotificationService notificationService;

    public StudentPresenter(UserDataProvider dataProvider, StudentViewCallback view, NotificationService notificationService) {
        super(dataProvider, view, notificationService);
        this.view = view;
        this.notificationService = notificationService;
    }

    @Override
    public void onExpel(List<Long> ids, String label, Object ... params) {
        view.confirmExpel(ids, () -> {
            notificationService.showNotification(NotificationType.EDIT, label, params);
        });
    }
}
