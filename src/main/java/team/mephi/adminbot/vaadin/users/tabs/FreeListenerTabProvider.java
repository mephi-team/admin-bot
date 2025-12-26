package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.FreeListenerDataProvider;
import team.mephi.adminbot.vaadin.users.views.FreeListenerView;

@SpringComponent
public class FreeListenerTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return 4;
    }

    @Override
    public String getTabId() {
        return "free_listener";
    }

    @Override
    public String getTabLabel() {
        return "Слушатели";
    }

    @Override
    public Component createTabContent(CRUDDataProvider<?> dataProvider, UserActions actions) {
        return new FreeListenerView((FreeListenerDataProvider) dataProvider, actions);
    }
}