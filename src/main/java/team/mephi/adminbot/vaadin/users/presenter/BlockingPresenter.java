package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.BlockingActions;

import java.util.List;

public class BlockingPresenter extends CRUDPresenter<SimpleUser> implements BlockingActions<SimpleUser> {
    private final UserDataProvider dataProvider;
    private final BlockingViewCallback view;
    public final NotificationService notificationService;

    public BlockingPresenter(UserDataProvider dataProvider, BlockingViewCallback view, NotificationService notificationService) {
        super(dataProvider, view, notificationService);
        this.dataProvider = dataProvider;
        this.view = view;
        this.notificationService = notificationService;
    }

    @Override
    public void onBlock(SimpleUser m, String label, Object ... params) {
        view.showDialogForBlock(m, (callback) -> {
            dataProvider.blockAllById(List.of(m.getId()));
            dataProvider.getDataProvider().refreshAll();
            notificationService.showNotification(NotificationType.DELETE, label, params);
        });
    }
}
