package team.mephi.adminbot.vaadin.mailings.actions;

import java.util.List;

public interface MailingActions {
    void onCreate(String role);
    void onView(Long id);
    void onEdit(Long id);
    void onDelete(List<Long> ids);
    void onAccept(List<Long> ids);
    void onReject(List<Long> ids);
}
