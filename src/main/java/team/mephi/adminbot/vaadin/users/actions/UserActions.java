package team.mephi.adminbot.vaadin.users.actions;

import java.util.List;

public interface UserActions {
    void onView(Long id);
    void onEdit(Long id);
    void onDelete(List<Long> ids);
    void onAccept(List<Long> ids);
    void onReject(List<Long> ids);
}