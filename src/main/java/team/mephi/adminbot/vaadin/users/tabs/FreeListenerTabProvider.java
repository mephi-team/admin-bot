package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.StudentPresenter;
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
    public Component createTabContent(CRUDActions actions) {
        return new FreeListenerView((StudentPresenter) actions);
    }
}