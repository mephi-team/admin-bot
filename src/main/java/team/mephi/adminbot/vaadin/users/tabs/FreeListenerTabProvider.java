package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.users.actions.StudentActions;
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
        return "page_users_tab_free_listener_label";
    }

    @Override
    public Component createTabContent(CRUDDataProvider<?> dataProvider, CRUDActions actions) {
        return new FreeListenerView((FreeListenerDataProvider) dataProvider, (StudentActions) actions);
    }
}