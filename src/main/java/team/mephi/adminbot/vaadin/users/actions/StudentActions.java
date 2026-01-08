package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.vaadin.service.DialogType;

import java.util.List;

public interface StudentActions extends UserActions {
    void onExpel(List<Long> ids, DialogType type, Object ... params);
}
