package team.mephi.adminbot.vaadin.mailings.tabs;

import com.vaadin.flow.component.Component;
import team.mephi.adminbot.vaadin.CRUDActions;

public interface MailingTabProvider {
    String getTabId();
    String getTabLabel();
    Component createTabContent(CRUDActions actions);
    Integer getPosition();
}
