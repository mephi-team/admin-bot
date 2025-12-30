package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.vaadin.users.actions.TutorActions;

public class TutorPresenter extends BlockingPresenter implements TutorActions {
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
}
