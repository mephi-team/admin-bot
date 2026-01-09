package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.BlockingActions;

import java.util.List;

public class BlockingPresenter extends CRUDPresenter<SimpleUser> implements BlockingActions<SimpleUser> {
    private final UserDataProvider dataProvider;
    private final DialogService<SimpleUser> dialogService;
    private final NotificationService notificationService;

    public BlockingPresenter(UserDataProvider dataProvider, DialogService<SimpleUser> dialogService, NotificationService notificationService) {
        super(dataProvider, dialogService, notificationService);
        this.dataProvider = dataProvider;
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    @Override
    public void onBlock(SimpleUser m, DialogType type, Object ... params) {
        dialogService.showDialog(m, type, (callback) -> {
            dataProvider.blockAllById(List.of(m.getId()));
            dataProvider.getDataProvider().refreshAll();
            notificationService.showNotification(NotificationType.DELETE, type.getNotificationKey(), params);
        });
    }
}
