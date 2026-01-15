package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.vaadin.service.DialogType;

import java.util.List;

/**
 * Интерфейс для действий, которые могут быть приняты или отклонены.
 */
public interface AcceptableActions {
    void onAccept(List<Long> ids, DialogType type, Object... params);

    void onReject(List<Long> ids, DialogType type, Object... params);
}
