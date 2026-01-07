package team.mephi.adminbot.vaadin;

import java.util.List;

public interface CRUDActions <T> {
    void onCreate(Object item, String label, Object ... param);
    void onView(T item, String label);
    void onEdit(T item, String label, Object ... param);
    void onDelete(List<Long> ids, String label, Object ... param);
}
