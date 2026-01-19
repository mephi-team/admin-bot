package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.vaadin.core.CRUDPresenter;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.BlockingActions;

import java.util.List;

/**
 * Презентер для управления блокировкой пользователей.
 * Расширяет общий CRUD-презентер и реализует действия по блокировке пользователей.
 */
public class BlockingPresenter extends CRUDPresenter<SimpleUser> implements BlockingActions<SimpleUser> {
    private final UserDataProvider dataProvider;
    private final DialogService<SimpleUser> dialogService;
    private final NotificationService notificationService;

    /**
     * Конструктор презентера для блокировки пользователей.
     *
     * @param dataProvider        Провайдер данных пользователей.
     * @param dialogService       Сервис для отображения диалогов.
     * @param notificationService Сервис для отображения уведомлений.
     */
    public BlockingPresenter(UserDataProvider dataProvider, DialogService<SimpleUser> dialogService, NotificationService notificationService) {
        super(dataProvider, dialogService, notificationService);
        this.dataProvider = dataProvider;
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    @Override
    public void onBlock(SimpleUser m, DialogType type, Object... params) {
        dialogService.showDialog(m, type, (user) -> {
            if (!user.getBlockReason().isEmpty()) {
                if (UserStatus.BLOCKED.name().equals(m.getStatus())) {
                    dataProvider.unblockAllById(List.of(m.getId()));
                    notificationService.showNotification(NotificationType.DELETE, type.getNotificationKey()+"_cancel", params);
                } else {
                    dataProvider.blockAllById(List.of(m.getId()));
                    notificationService.showNotification(NotificationType.DELETE, type.getNotificationKey(), params);
                }
                dataProvider.getDataProvider().refreshAll();
            } else {
                notificationService.showNotification(NotificationType.DELETE, "notification_user_warning");
            }
        });
    }
}
