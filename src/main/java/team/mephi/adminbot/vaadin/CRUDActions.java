package team.mephi.adminbot.vaadin;

import java.util.List;

public interface CRUDActions {
    void onCreate(String role);
    void onView(Long id);
    void onEdit(Long id);
    void onDelete(List<Long> ids);
}
