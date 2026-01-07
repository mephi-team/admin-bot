package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;
import team.mephi.adminbot.vaadin.users.actions.TutorActions;

import java.util.Objects;

public class TutorPresenter extends BlockingPresenter implements TutorActions {
    private final UserDataProvider dataProvider;
    private final TutorViewCallback view;

    public TutorPresenter(UserDataProvider dataProvider, TutorViewCallback view, NotificationService notificationService) {
        super(dataProvider, view, notificationService);
        this.dataProvider = dataProvider;
        this.view = view;
    }

    @Override
    public void onTutoring(SimpleUser item, String label, Object ... params) {
        view.showDialogForTutoring(item, editedItem -> {
            if (Objects.nonNull(editedItem)) {
                editedItem = dataProvider.save(editedItem);
                dataProvider.getDataProvider().refreshItem(editedItem);
                notificationService.showNotification(NotificationType.EDIT, label, params);
            }
        });
    }
}
