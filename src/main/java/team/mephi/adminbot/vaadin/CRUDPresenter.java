package team.mephi.adminbot.vaadin;

import java.util.List;

public class CRUDPresenter<T> implements CRUDActions, DataProvider<T> {
    private final CRUDDataProvider<T> dataProvider;
    protected final CRUDViewCallback<T> view;

    public CRUDPresenter(CRUDDataProvider<T> dataProvider, CRUDViewCallback<T> view) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onCreate(String role) {
        view.showDialogForNew(role, () -> {
            var newMailing = view.getEditedItem();
            if (newMailing != null) {
                dataProvider.save((T) newMailing);
                dataProvider.getDataProvider().refreshAll();
                view.showNotificationForNew();
            }
        });
    }

    @Override
    public void onView(Long id) {
        dataProvider.findById(id).ifPresent(view::showDialogForView);
    }

    @Override
    public void onEdit(Long id) {
        dataProvider.findById(id).ifPresent(m -> {
            view.showDialogForEdit(m, () -> {
                var editedItem = view.getEditedItem();
                if (editedItem != null) {
                    dataProvider.save((T) editedItem);
                    dataProvider.getDataProvider().refreshAll();
                    view.showNotificationForEdit(id);
                }
            });
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
