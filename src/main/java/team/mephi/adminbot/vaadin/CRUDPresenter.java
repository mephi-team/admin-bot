package team.mephi.adminbot.vaadin;

import java.util.List;

public class CRUDPresenter<T> implements CRUDActions<T>, DataProvider<T> {
    private final CRUDDataProvider<T> dataProvider;
    protected final CRUDViewCallback<T> view;

    public CRUDPresenter(CRUDDataProvider<T> dataProvider, CRUDViewCallback<T> view) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onCreate(String role) {
        view.showDialogForNew(role, (newMailing) -> {
            if (newMailing != null) {
                System.out.println("!!! onCreate " + newMailing);
                dataProvider.save((T) newMailing);
                dataProvider.getDataProvider().refreshAll();
                view.showNotificationForNew();
            }
        });
    }

    @Override
    public void onView(T user) {
        view.showDialogForView(user);
    }

    @Override
    public void onEdit(T item) {
        view.showDialogForEdit(item, (editedItem) -> {
            if (editedItem != null) {
                editedItem = dataProvider.save((T) editedItem);
                dataProvider.getDataProvider().refreshItem((T) editedItem);
                view.showNotificationForEdit(0L);
            }
        });
    }

    @Override
    public void onDelete(List<Long> ids) {
        view.confirmDelete(ids, () -> {
            dataProvider.deleteAllById(ids);
            dataProvider.getDataProvider().refreshAll();
            view.showNotificationForDelete(ids);
        });
    }

    @Override
    public CRUDDataProvider<T> getDataProvider() {
        return this.dataProvider;
    }
}
