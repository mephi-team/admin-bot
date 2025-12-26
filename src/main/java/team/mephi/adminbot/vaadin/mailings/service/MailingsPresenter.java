package team.mephi.adminbot.vaadin.mailings.service;

import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.MailingDataProvider;


public class MailingsPresenter extends CRUDPresenter<SimpleMailing> implements MailingActions {
    private final MailingViewCallback view;
    private final MailingDataProvider<SimpleMailing> dataProvider;
    public MailingsPresenter(MailingDataProvider<SimpleMailing> dataProvider, MailingViewCallback view) {
        super(dataProvider, view);
        this.view = view;
        this.dataProvider = dataProvider;
    }

    @Override
    public void onCancel(Long id) {
        view.confirmCancel(id, () -> {
            var edit = dataProvider.findById(id).orElseThrow();
            edit.setStatus("FINISHED");
            dataProvider.save(edit);
            dataProvider.getDataProvider().refreshAll();
            view.showNotificationForCancel(id);
        });
    }

    @Override
    public void onRetry(Long id) {
        view.confirmRetry(id, () -> {
            var edit = dataProvider.findById(id).orElseThrow();
            edit.setStatus("ACTIVE");
            dataProvider.save(edit);
            dataProvider.getDataProvider().refreshAll();
            view.showNotificationForRetry(id);
        });
    }
}
