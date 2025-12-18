package team.mephi.adminbot.vaadin.mailings.service;

import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.MailingDataProvider;

import java.util.List;

public class MailingsPresenter  implements MailingActions {
    private static final String DELETE_MESSAGE = "Рассылка удалена";
    private static final String DELETE_ALL_MESSAGE = "Удалено %d рассылок";

    private static final String MAILING_CREATED = "Рассылка сохранена";
    private static final String MAILING_SAVED = "Рассылка сохранена";

    private final MailingDataProvider<SimpleMailing> dataProvider;
    private final MailingViewCallback<SimpleMailing> view;

    public MailingsPresenter(MailingDataProvider<SimpleMailing> dataProvider, MailingViewCallback<SimpleMailing> view) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onCreate(String role) {
        view.showUserEditorForNew(role);
        view.setOnSaveCallback(() -> {
            SimpleMailing newMailing = view.getEditedMailing();
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
            view.showUserEditorForEdit(m);
            view.setOnSaveCallback(() -> {
                SimpleMailing editedMailing = view.getEditedMailing();
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

    @Override
    public void onAccept(List<Long> ids) {

    }

    @Override
    public void onReject(List<Long> ids) {

    }

    private String makeNotification(String single, String plural, int count) {
        if (count > 1) {
            return String.format(plural, count);
        }
        return single;
    }
}
