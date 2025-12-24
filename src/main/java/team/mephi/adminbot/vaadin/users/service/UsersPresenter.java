package team.mephi.adminbot.vaadin.users.service;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.users.actions.UserActions;

import java.util.List;

public class UsersPresenter extends CRUDPresenter<SimpleUser> implements UserActions {
    private final UserViewCallback view;

    public UsersPresenter(CRUDDataProvider<SimpleUser> dataProvider, UserViewCallback view) {
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