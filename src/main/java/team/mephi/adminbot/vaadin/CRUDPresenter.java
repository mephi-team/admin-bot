package team.mephi.adminbot.vaadin;

import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;

import java.util.List;

public class CRUDPresenter<T> implements CRUDActions<T>, DataProvider<T> {
    private final CRUDDataProvider<T> dataProvider;
    protected final CRUDViewCallback<T> view;
    private final NotificationService notificationService;

    public CRUDPresenter(CRUDDataProvider<T> dataProvider, CRUDViewCallback<T> view, NotificationService notificationService) {
        this.dataProvider = dataProvider;
        this.view = view;
        this.notificationService = notificationService;
    }

    @Override
    public void onCreate(String role, String label, Object ... params) {
        view.showDialogForNew(role, (newMailing) -> {
            if (newMailing != null) {
                dataProvider.save((T) newMailing);
                dataProvider.getDataProvider().refreshAll();
                notificationService.showNotification(NotificationType.NEW, label, params);
            }
        });
    }

    @Override
    public void onView(T user) {
        view.showDialogForView(user);
    }

    @Override
    public void onEdit(T item, String label, Object ... params) {
        view.showDialogForEdit(item, (editedItem) -> {
            if (editedItem != null) {
                editedItem = dataProvider.save((T) editedItem);
                dataProvider.getDataProvider().refreshItem((T) editedItem);
                notificationService.showNotification(NotificationType.EDIT, label, params);
            }
        });
    }

    @Override
    public void onDelete(List<Long> ids, String label, Object ... param) {
        view.confirmDelete(ids, () -> {
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
