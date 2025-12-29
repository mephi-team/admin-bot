package team.mephi.adminbot.vaadin.users.actions;

import java.util.List;

public interface AcceptableActions {
    void onAccept(List<Long> ids);
    void onReject(List<Long> ids);
}
