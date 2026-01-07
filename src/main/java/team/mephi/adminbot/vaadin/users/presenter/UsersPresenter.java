package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.UserActions;

import java.util.List;

public class UsersPresenter extends BlockingPresenter implements UserActions {
    private final UserViewCallback view;
    private final NotificationService notificationService;

    public UsersPresenter(UserDataProvider dataProvider, UserViewCallback view, NotificationService notificationService) {
        super(dataProvider, view, notificationService);
        this.view = view;
        this.notificationService = notificationService;
    }

    @Override
    public void onAccept(List<Long> ids, String label, Object ... params) {
        view.confirmAccept(ids, () -> {
            notificationService.showNotification(NotificationType.EDIT, label, params);
        });
    }

    @Override
    public void onReject(List<Long> ids, String label, Object ... params) {
        view.confirmReject(ids, () -> {
            notificationService.showNotification(NotificationType.EDIT, label, params);
        });
    }
}