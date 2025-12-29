package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

import java.util.List;

public class UsersPresenter extends BlockingPresenter implements UserActions {
    private final UserViewCallback view;

    public UsersPresenter(UserDataProvider dataProvider, UserViewCallback view) {
        super(dataProvider, view);
        this.view = view;
    }

    @Override
    public void onAccept(List<Long> ids) {
        view.confirmAccept(ids, () -> {
            view.showNotificationForAccept(ids);
        });
    }

    @Override
    public void onReject(List<Long> ids) {
        view.confirmReject(ids, () -> {
            view.showNotificationForReject(ids);
        });
    }
}