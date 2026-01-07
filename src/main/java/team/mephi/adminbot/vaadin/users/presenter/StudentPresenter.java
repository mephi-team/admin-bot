package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.StudentActions;

import java.util.List;

public class StudentPresenter extends UsersPresenter implements StudentActions {
    private final DialogService<?> dialogService;
    private final NotificationService notificationService;

    public StudentPresenter(UserDataProvider dataProvider, DialogService<SimpleUser> dialogService, NotificationService notificationService) {
        super(dataProvider, dialogService, notificationService);
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    @Override
    public void onExpel(List<Long> ids, String label, Object ... params) {
        dialogService.showConfirmDialog(ids.size(), label, (ignore) -> {
            notificationService.showNotification(NotificationType.EDIT, label, params);
        });
    }
}
