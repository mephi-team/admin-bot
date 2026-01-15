package team.mephi.adminbot.vaadin;

import com.vaadin.flow.component.icon.VaadinIcon;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;

import java.util.List;

/**
 * Класс CRUDPresenter реализует интерфейсы CRUDActions и HasDataProvider для управления операциями CRUD над сущностями типа T.
 *
 * @param <T> Тип сущности, над которой выполняются операции CRUD.
 */
public class CRUDPresenter<T> implements CRUDActions<T>, HasDataProvider<CRUDDataProvider<T>> {
    private final CRUDDataProvider<T> dataProvider;
    private final DialogService<T> dialogService;
    private final NotificationService notificationService;

    public CRUDPresenter(CRUDDataProvider<T> dataProvider, DialogService<T> dialogService, NotificationService notificationService) {
        this.dataProvider = dataProvider;
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    @Override
    public void onCreate(Object item, DialogType type, Object... params) {
        dialogService.showDialog(item, type, (newMailing) -> {
            if (newMailing != null) {
                dataProvider.save(newMailing);
                dataProvider.getDataProvider().refreshAll();
                notificationService.showNotification(NotificationType.NEW, type.getNotificationKey(), params);
            }
        });
    }

    @Override
    public void onView(T item, DialogType type) {
        dialogService.showDialog(item, type, null);
    }

    @Override
    public void onEdit(T item, DialogType type, Object... params) {
        dialogService.showDialog(item, type, (editedItem) -> {
            if (editedItem != null) {
                editedItem = dataProvider.save(editedItem);
                dataProvider.getDataProvider().refreshItem(editedItem);
                notificationService.showNotification(NotificationType.EDIT, type.getNotificationKey(), params);
            }
        });
    }

    @Override
    public void onDelete(List<Long> ids, DialogType type, Object... param) {
        dialogService.showConfirmDialog(ids.size(), type, VaadinIcon.TRASH.create(), (ignore) -> {
            dataProvider.deleteAllById(ids);
            dataProvider.getDataProvider().refreshAll();
            notificationService.showNotification(NotificationType.DELETE, type.getNotificationKey(), param);
        });
    }

    @Override
    public CRUDDataProvider<T> getDataProvider() {
        return this.dataProvider;
    }
}
