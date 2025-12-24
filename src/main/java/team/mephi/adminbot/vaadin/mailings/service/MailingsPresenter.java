package team.mephi.adminbot.vaadin.mailings.service;

import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.MailingDataProvider;

import java.util.List;

public class MailingsPresenter <T> implements CRUDActions {
    private static final String DELETE_MESSAGE = "Рассылка удалена";
    private static final String DELETE_ALL_MESSAGE = "Удалено %d рассылок";

    private static final String MAILING_CREATED = "Рассылка сохранена";
    private static final String MAILING_SAVED = "Рассылка сохранена";

    private final MailingDataProvider<T> dataProvider;
    private final MailingViewCallback<T> view;

    public MailingsPresenter(MailingDataProvider<T> dataProvider, MailingViewCallback<T> view) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onCreate(String role) {
        view.showDialogForNew(role);
        view.setOnSaveCallback(() -> {
            T newMailing = view.getEditedMailing();
            if (newMailing != null) {
                dataProvider.save(newMailing);
                dataProvider.refresh();
                view.showNotification(MAILING_CREATED);
            }
        });
    }

    @Override
    public void onView(Long id) {

    }

    @Override
    public void onEdit(Long id) {
        dataProvider.findById(id).ifPresent(m -> {
            view.showDialogForEdit(m);
            view.setOnSaveCallback(() -> {
                var editedMailing = view.getEditedMailing();
                if (editedMailing != null) {
                    dataProvider.save(editedMailing);
                    dataProvider.refresh();
                    view.showNotification(MAILING_SAVED);
                }
            });
        });
    }

    @Override
    public void onDelete(List<Long> ids) {
        view.confirmDelete(ids, () -> {
            dataProvider.deleteAllById(ids);
            dataProvider.refresh();
            view.showNotification(makeNotification(DELETE_MESSAGE, DELETE_ALL_MESSAGE, ids.size()));
        });
    }

    private String makeNotification(String single, String plural, int count) {
        if (count > 1) {
            return String.format(plural, count);
        }
        return single;
    }
}
