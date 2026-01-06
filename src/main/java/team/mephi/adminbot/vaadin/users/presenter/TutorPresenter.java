package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.users.actions.TutorActions;

import java.util.Objects;

public class TutorPresenter extends BlockingPresenter implements TutorActions {
    private final UserDataProvider dataProvider;
    private final TutorViewCallback view;

    public TutorPresenter(UserDataProvider dataProvider, TutorViewCallback view) {
        super(dataProvider, view);
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onTutoring(SimpleUser item) {
        view.showDialogForTutoring(item, editedItem -> {
            if (Objects.nonNull(editedItem)) {
                editedItem = dataProvider.save(editedItem);
                dataProvider.getDataProvider().refreshItem(editedItem);
                view.showNotificationForTutoring(item.getId());
            }
        });
    }
}
