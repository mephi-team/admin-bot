package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;

public interface MailingTabProvider {
    String getTabId();
    String getTabLabel();
    Component createTabContent(MailingActions actions);
    Integer getPosition();
}
