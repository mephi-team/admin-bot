package team.mephi.adminbot.vaadin;

import com.vaadin.flow.component.Component;

public interface TabProvider<T> {
    String getTabId();
    String getTabLabel();
    Component createTabContent(T actions);
    Integer getPosition();
}
