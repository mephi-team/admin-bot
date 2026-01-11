package team.mephi.adminbot.vaadin.mailings.presenter;

import com.vaadin.flow.component.icon.VaadinIcon;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;

public class MailingsPresenter extends CRUDPresenter<SimpleMailing> implements MailingActions {
    private final CRUDDataProvider<SimpleMailing> dataProvider;
    private final DialogService<SimpleMailing> dialogService;
    private final NotificationService notificationService;

    public MailingsPresenter(CRUDDataProvider<SimpleMailing> dataProvider, DialogService<SimpleMailing> dialogService, NotificationService notificationService) {
        super(dataProvider, dialogService, notificationService);
        this.dataProvider = dataProvider;
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    @Override
    public void onCancel(SimpleMailing item, DialogType type, Object ... params) {
        dialogService.showConfirmDialog(item, type, VaadinIcon.CLOSE_CIRCLE_O.create(), (edit) -> {
            edit.setStatus("CANCELED");
            dataProvider.save(edit);
            dataProvider.getDataProvider().refreshItem(edit);
            notificationService.showNotification(NotificationType.EDIT, type.getNotificationKey(), params);
        });
    }

    @Override
    public void onRetry(SimpleMailing item, DialogType type, Object ... params) {
        dialogService.showConfirmDialog(item, type, VaadinIcon.ROTATE_RIGHT.create(), (edit) -> {
            edit.setStatus("ACTIVE");
            dataProvider.save(edit);
            dataProvider.getDataProvider().refreshItem(edit);
            notificationService.showNotification(NotificationType.EDIT, type.getNotificationKey(), params);
        });
    }
}
