package team.mephi.adminbot.vaadin.users.actions;

import java.util.List;

public interface AcceptableActions {
    void onAccept(List<Long> ids, String label, Object ... params);
    void onReject(List<Long> ids, String label, Object ... params);
}
