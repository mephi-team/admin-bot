package team.mephi.adminbot.vaadin;

import java.util.List;

public class CRUDPresenter<T> implements CRUDActions {
    private final CRUDDataProvider<T> dataProvider;
    protected final CRUDViewCallback<T> view;

    public CRUDPresenter(CRUDDataProvider<T> dataProvider, CRUDViewCallback<T> view) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onCreate(String role) {
        view.showDialogForNew(role);
        view.setOnSaveCallback(() -> {
            T newMailing = view.getEditedItem();
            if (newMailing != null) {
                dataProvider.save(newMailing);
                dataProvider.refresh();
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
            view.showDialogForEdit(m);
            view.setOnSaveCallback(() -> {
                T editedMailing = view.getEditedItem();
                if (editedMailing != null) {
                    dataProvider.save(editedMailing);
                    dataProvider.refresh();
                    view.showNotificationForEdit(id);
                }
            });
        });
    }

    @Override
    public void onDelete(List<Long> ids) {
        view.confirmDelete(ids, () -> {
            dataProvider.deleteAllById(ids);
            dataProvider.refresh();
            view.showNotificationForDelete(ids);
        });
    }
}
