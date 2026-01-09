package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.vaadin.service.DialogType;

public interface BlockingActions <T> {
    void onBlock(T item, DialogType type, Object ... params);
}
