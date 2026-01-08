package team.mephi.adminbot.vaadin.mailings.actions;

import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.service.DialogType;

public interface MailingActions extends CRUDActions<SimpleMailing> {
    void onCancel(SimpleMailing ids, DialogType type, Object ... params);
    void onRetry(SimpleMailing ids, DialogType type, Object ... params);
}
