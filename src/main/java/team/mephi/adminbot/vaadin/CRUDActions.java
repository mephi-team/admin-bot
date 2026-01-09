package team.mephi.adminbot.vaadin;

import team.mephi.adminbot.vaadin.service.DialogType;

import java.util.List;

public interface CRUDActions <T> {
    void onCreate(Object item, DialogType type, Object ... param);
    void onView(T item, DialogType type);
    void onEdit(T item, DialogType type, Object ... param);
    void onDelete(List<Long> ids, DialogType type, Object ... param);
}
