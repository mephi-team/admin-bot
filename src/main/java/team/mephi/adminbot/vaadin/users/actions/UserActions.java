package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.vaadin.CRUDActions;

import java.util.List;

public interface UserActions extends CRUDActions {
    void onAccept(List<Long> ids);
    void onReject(List<Long> ids);
    void onBlock(Long id);
    void onExpel(List<Long> ids);
}