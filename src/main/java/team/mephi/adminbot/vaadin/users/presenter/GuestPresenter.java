package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.vaadin.users.actions.GuestActions;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

public class GuestPresenter extends BlockingPresenter implements GuestActions {
    public GuestPresenter(UserDataProvider dataProvider, BlockingViewCallback view) {
        super(dataProvider, view);
    }
}
