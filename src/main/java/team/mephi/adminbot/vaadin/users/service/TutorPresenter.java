package team.mephi.adminbot.vaadin.users.service;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.users.actions.TutorActions;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

import java.util.List;

public class TutorPresenter extends CRUDPresenter<SimpleUser> implements TutorActions {
    private final UserDataProvider dataProvider;
    private final TutorViewCallback view;

    public TutorPresenter(UserDataProvider dataProvider, TutorViewCallback view) {
        super(dataProvider, view);
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onTutoring(Long id) {
        dataProvider.findById(id).ifPresent(view::showDialogForTutoring);
//        view.showNotificationForTutoring(id);
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
}
