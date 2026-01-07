package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.TutorActions;

import java.util.Objects;

public class TutorPresenter extends BlockingPresenter implements TutorActions {
    private final UserDataProvider dataProvider;
    private final DialogService<SimpleUser> dialogService;
    private final NotificationService notificationService;

    public TutorPresenter(UserDataProvider dataProvider, DialogService<SimpleUser> dialogService, NotificationService notificationService) {
        super(dataProvider, dialogService, notificationService);
        this.dataProvider = dataProvider;
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    @Override
    public void onTutoring(SimpleUser item, String label, Object ... params) {
        dialogService.showDialog(item, label, editedItem -> {
            if (Objects.nonNull(editedItem)) {
                editedItem = dataProvider.save(editedItem);
                dataProvider.getDataProvider().refreshItem(editedItem);
                notificationService.showNotification(NotificationType.EDIT, label, params);
            }
        });
    }
}
