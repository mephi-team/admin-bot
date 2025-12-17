package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;

public interface UserTabProvider {
    String getTabId();
    String getTabLabel();
    Component createTabContent(UserActions actions);
    Integer getPosition();
}