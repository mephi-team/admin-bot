package team.mephi.adminbot.vaadin.mailings.presenter;

import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;


public class MailingsPresenter extends CRUDPresenter<SimpleMailing> implements MailingActions {
    private final MailingViewCallback view;
    private final CRUDDataProvider<SimpleMailing> dataProvider;
    public MailingsPresenter(CRUDDataProvider<SimpleMailing> dataProvider, MailingViewCallback view) {
        super(dataProvider, view);
        this.view = view;
        this.dataProvider = dataProvider;
    }

    @Override
    public void onCancel(SimpleMailing item) {
        view.confirmCancel(item, (edit) -> {
            edit.setStatus("FINISHED");
            dataProvider.save(edit);
            dataProvider.getDataProvider().refreshItem(edit);
            view.showNotificationForCancel(edit.getId());
        });
    }

    @Override
    public void onRetry(SimpleMailing item) {
        view.confirmRetry(item, (edit) -> {
            edit.setStatus("ACTIVE");
            dataProvider.save(edit);
            dataProvider.getDataProvider().refreshItem(edit);
            view.showNotificationForRetry(edit.getId());
        });
    }
}
