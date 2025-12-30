package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.users.actions.BlockingActions;

import java.util.List;

public class BlockingPresenter extends CRUDPresenter<SimpleUser> implements BlockingActions {
    private final UserDataProvider dataProvider;
    private final BlockingViewCallback view;

    public BlockingPresenter(UserDataProvider dataProvider, BlockingViewCallback view) {
        super(dataProvider, view);
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onBlock(Long id) {
        dataProvider.findById(id).ifPresent(m -> {
            view.showDialogForBlock(m, (callback) -> {
                dataProvider.blockAllById(List.of(m.getId()));
                dataProvider.getDataProvider().refreshAll();
                view.showNotificationForBlock(m.getId());
            });
        });
    }
}
