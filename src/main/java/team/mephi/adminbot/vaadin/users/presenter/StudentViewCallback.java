package team.mephi.adminbot.vaadin.users.presenter;

import java.util.List;

public interface StudentViewCallback extends UserViewCallback {
    void confirmExpel(List<Long> ids, Runnable onConfirm);
}
