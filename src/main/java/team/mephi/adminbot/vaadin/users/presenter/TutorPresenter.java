package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.TutorActions;

import java.util.List;
import java.util.Objects;

/**
 * Презентер для управления преподавателями.
 * Расширяет общий CRUD-презентер и реализует действия, специфичные для преподавателей.
 */
public class TutorPresenter extends CRUDPresenter<SimpleTutor> implements TutorActions {
    private final TutorDataProvider dataProvider;
    private final DialogService<SimpleTutor> dialogService;
    private final NotificationService notificationService;

    public TutorPresenter(TutorDataProvider dataProvider, DialogService<SimpleTutor> dialogService, NotificationService notificationService) {
        super(dataProvider, dialogService, notificationService);
        this.dataProvider = dataProvider;
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    @Override
    public void onTutoring(SimpleTutor item, DialogType type, Object... params) {
        dialogService.showDialog(item, type, editedItem -> {
            if (Objects.nonNull(editedItem)) {
                editedItem = dataProvider.save(editedItem);
                dataProvider.getDataProvider().refreshItem(editedItem);
                notificationService.showNotification(NotificationType.EDIT, type.getNotificationKey(), params);
            }
        });
    }

    @Override
    public void onBlock(SimpleTutor item, DialogType type, Object... params) {
        dialogService.showDialog(item, type, (ignoredCallback) -> {
            dataProvider.blockAllById(List.of(item.getId()));
            dataProvider.getDataProvider().refreshAll();
            notificationService.showNotification(NotificationType.DELETE, type.getNotificationKey(), params);
        });
    }
}
