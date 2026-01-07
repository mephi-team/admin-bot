package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.BlockingPresenter;
import team.mephi.adminbot.vaadin.users.views.GuestsView;

@SpringComponent
public class GuestsTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return 0;
    }

    @Override
    public String getTabId() {
        return "visitor";
    }

    @Override
    public String getTabLabel() {
        return "page_users_tab_visitor_label";
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new GuestsView((BlockingPresenter) actions);
    }
}