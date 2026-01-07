package team.mephi.adminbot.vaadin.mailings.presenter;

import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;


public class MailingsPresenter extends CRUDPresenter<SimpleMailing> implements MailingActions {
    private final MailingViewCallback view;
    private final CRUDDataProvider<SimpleMailing> dataProvider;
    private final NotificationService notificationService;

    public MailingsPresenter(CRUDDataProvider<SimpleMailing> dataProvider, MailingViewCallback view, NotificationService notificationService) {
        super(dataProvider, view, notificationService);
        this.view = view;
        this.dataProvider = dataProvider;
        this.notificationService = notificationService;
    }

    @Override
    public void onCancel(SimpleMailing item, String label, Object ... params) {
        view.confirmCancel(item, (edit) -> {
            edit.setStatus("CANCELED");
            dataProvider.save(edit);
            dataProvider.getDataProvider().refreshItem(edit);
            notificationService.showNotification(NotificationType.EDIT, label, params);
        });
    }

    @Override
    public void onRetry(SimpleMailing item, String label, Object ... params) {
        view.confirmRetry(item, (edit) -> {
            edit.setStatus("ACTIVE");
            dataProvider.save(edit);
            dataProvider.getDataProvider().refreshItem(edit);
            notificationService.showNotification(NotificationType.EDIT, label, params);
        });
    }
}
