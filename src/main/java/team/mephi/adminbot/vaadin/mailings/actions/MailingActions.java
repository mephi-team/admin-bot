package team.mephi.adminbot.vaadin.mailings.actions;

import java.util.List;

public interface MailingActions {
    void onCreate(String role);
    void onEdit(Long id);
    void onDelete(List<Long> ids);
}
