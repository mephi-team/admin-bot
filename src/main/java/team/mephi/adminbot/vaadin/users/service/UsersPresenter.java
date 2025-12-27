package team.mephi.adminbot.vaadin.users.service;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

import java.util.List;

public class UsersPresenter extends CRUDPresenter<SimpleUser> implements UserActions {
    private final UserDataProvider dataProvider;
    private final UserViewCallback view;

    public UsersPresenter(UserDataProvider dataProvider, UserViewCallback view) {
        super(dataProvider, view);
        this.dataProvider = dataProvider;
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

    @Override
    public void onBlock(Long id) {
        dataProvider.findById(id).ifPresent(m -> {
            view.showDialogForBlock(m);
            view.setOnSaveCallback(() -> {
                dataProvider.blockAllById(List.of(m.getId()));
                dataProvider.getDataProvider().refreshAll();
                view.showNotificationForBlock(m.getId());
            });
        });
    }

    @Override
    public void onExpel(List<Long> ids) {
        view.confirmExpel(ids, () -> {
            view.showNotificationForExpel(ids);
        });
    }
}