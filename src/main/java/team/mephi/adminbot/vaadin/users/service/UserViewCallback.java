package team.mephi.adminbot.vaadin.users.service;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDViewCallback;

import java.util.List;

public interface UserViewCallback extends CRUDViewCallback<SimpleUser> {
    void confirmAccept(List<Long> ids, Runnable onConfirm);
    void confirmReject(List<Long> ids, Runnable onConfirm);
    void showNotificationForAccept(List<Long> ids);
    void showNotificationForReject(List<Long> ids);
}
