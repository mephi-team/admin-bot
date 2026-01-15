package team.mephi.adminbot.vaadin.users.presenter;

import com.vaadin.flow.component.icon.VaadinIcon;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.StudentActions;

import java.util.List;

/**
 * Презентер для управления студентами.
 * Расширяет общий презентер пользователей и реализует действия, специфичные для студентов.
 */
public class StudentPresenter extends UsersPresenter implements StudentActions {
    private final DialogService<?> dialogService;
    private final NotificationService notificationService;

    /**
     * Конструктор презентера для студентов.
     *
     * @param dataProvider        Провайдер данных пользователей.
     * @param dialogService       Сервис для отображения диалогов.
     * @param notificationService Сервис для отображения уведомлений.
     */
    public StudentPresenter(UserDataProvider dataProvider, DialogService<SimpleUser> dialogService, NotificationService notificationService) {
        super(dataProvider, dialogService, notificationService);
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    @Override
    public void onExpel(List<Long> ids, DialogType type, Object... params) {
        dialogService.showConfirmDialog(ids.size(), type, VaadinIcon.CLOSE.create(), (ignore) -> notificationService.showNotification(NotificationType.EDIT, type.getNotificationKey(), params));
    }
}
