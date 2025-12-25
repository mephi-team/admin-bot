package team.mephi.adminbot.vaadin.mailings.actions;

import team.mephi.adminbot.vaadin.CRUDActions;

public interface MailingActions extends CRUDActions {
    void onCancel(Long ids);
    void onRetry(Long ids);
}
