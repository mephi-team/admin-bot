package team.mephi.adminbot.vaadin.mailings.service;

import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.SentDataProvider;

import java.util.List;

public class MailingsPresenter  implements MailingActions {
    private static final String DELETE_MESSAGE = "Рассылка удалена";
    private static final String DELETE_ALL_MESSAGE = "Удалено %d рассылок";

    private final SentDataProvider dataProvider;
    private final MailingViewCallback view;

    public MailingsPresenter(SentDataProvider dataProvider, MailingViewCallback view) {
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onCreate(String role) {

    }

    @Override
    public void onView(Long id) {

    }

    @Override
    public void onEdit(Long id) {

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
