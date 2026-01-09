package team.mephi.adminbot.vaadin.users.presenter;

import com.vaadin.flow.component.icon.VaadinIcon;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.UserActions;

import java.util.List;

public class UsersPresenter extends BlockingPresenter implements UserActions {
    private final DialogService<?> dialogService;
    private final NotificationService notificationService;

    public UsersPresenter(UserDataProvider dataProvider, DialogService<SimpleUser> dialogService, NotificationService notificationService) {
        super(dataProvider, dialogService, notificationService);
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    @Override
    public void onAccept(List<Long> ids, DialogType type, Object ... params) {
        dialogService.showConfirmDialog(ids.size(), type, VaadinIcon.CHECK.create(), (ignore) -> {
            notificationService.showNotification(NotificationType.EDIT, type.getNotificationKey(), params);
        });
    }

    @Override
    public void onReject(List<Long> ids, DialogType type, Object ... params) {
        dialogService.showConfirmDialog(ids.size(), type, VaadinIcon.CLOSE.create(), (ignore) -> {
            notificationService.showNotification(NotificationType.EDIT, type.getNotificationKey(), params);
        });
    }
}