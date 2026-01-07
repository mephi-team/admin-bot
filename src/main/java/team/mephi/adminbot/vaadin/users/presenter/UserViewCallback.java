package team.mephi.adminbot.vaadin.users.presenter;

import java.util.List;

public interface UserViewCallback extends BlockingViewCallback {
    void confirmAccept(List<Long> ids, Runnable onConfirm);
    void confirmReject(List<Long> ids, Runnable onConfirm);
}
