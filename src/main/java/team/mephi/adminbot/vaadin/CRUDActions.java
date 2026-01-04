package team.mephi.adminbot.vaadin;

import java.util.List;

public interface CRUDActions <T> {
    void onCreate(String type);
    void onView(T item);
    void onEdit(T item);
    void onDelete(List<Long> ids);
}
