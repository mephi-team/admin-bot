package team.mephi.adminbot.vaadin;

import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;

import java.util.List;

public class CRUDPresenter<T> implements CRUDActions<T>, DataProvider<T> {
    private final CRUDDataProvider<T> dataProvider;
    private final DialogService<T> dialogService;
    private final NotificationService notificationService;

    public CRUDPresenter(CRUDDataProvider<T> dataProvider, DialogService<T> dialogService, NotificationService notificationService) {
        this.dataProvider = dataProvider;
        this.dialogService = dialogService;
        this.notificationService = notificationService;
    }

    @Override
    public void onCreate(Object item, String label, Object ... params) {
        dialogService.showDialog(item, label, (newMailing) -> {
            if (newMailing != null) {
                dataProvider.save(newMailing);
                dataProvider.getDataProvider().refreshAll();
                notificationService.showNotification(NotificationType.NEW, label, params);
            }
        });
    }

    @Override
    public void onView(T item, String label) {
        dialogService.showDialog(item, label, null);
    }

    @Override
    public void onEdit(T item, String label, Object ... params) {
        dialogService.showDialog(item, label, (editedItem) -> {
            if (editedItem != null) {
                editedItem = dataProvider.save(editedItem);
                dataProvider.getDataProvider().refreshItem((T) editedItem);
                notificationService.showNotification(NotificationType.EDIT, label, params);
            }
        });
    }

    @Override
    public void onDelete(List<Long> ids, String label, Object ... param) {
        dialogService.showConfirmDialog(ids.size(), label, (ignore) -> {
            dataProvider.deleteAllById(ids);
            dataProvider.getDataProvider().refreshAll();
            notificationService.showNotification(NotificationType.DELETE, label, param);
        });
    }

    @Override
    public CRUDDataProvider<T> getDataProvider() {
        return this.dataProvider;
    }
}
