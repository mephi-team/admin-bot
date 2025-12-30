package team.mephi.adminbot.vaadin;

import java.util.List;

public interface CRUDActions <T> {
    void onCreate(String role);
    void onView(T id);
    void onEdit(T id);
    void onDelete(List<Long> ids);
}
