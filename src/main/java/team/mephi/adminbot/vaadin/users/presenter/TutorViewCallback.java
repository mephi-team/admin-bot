package team.mephi.adminbot.vaadin.users.presenter;

import com.vaadin.flow.function.SerializableConsumer;
import team.mephi.adminbot.dto.SimpleUser;

public interface TutorViewCallback extends BlockingViewCallback {
    void showDialogForTutoring(SimpleUser user, SerializableConsumer<SimpleUser> callback);
}
