package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.vaadin.users.actions.StudentActions;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

import java.util.List;

public class StudentPresenter extends UsersPresenter implements StudentActions {
    private final StudentViewCallback view;

    public StudentPresenter(UserDataProvider dataProvider, StudentViewCallback view) {
        super(dataProvider, view);
        this.view = view;
    }

    @Override
    public void onExpel(List<Long> ids) {
        view.confirmExpel(ids, () -> {
            view.showNotificationForExpel(ids);
        });
    }
}
