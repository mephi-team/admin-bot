package team.mephi.adminbot.vaadin.mailings.actions;

import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.CRUDActions;

public interface MailingActions extends CRUDActions<SimpleMailing> {
    void onCancel(SimpleMailing ids);
    void onRetry(SimpleMailing ids);
}
