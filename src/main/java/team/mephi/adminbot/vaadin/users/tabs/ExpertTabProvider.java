package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.UsersPresenter;
import team.mephi.adminbot.vaadin.users.views.ExpertView;

@SpringComponent
public class ExpertTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return 5;
    }

    @Override
    public String getTabId() {
        return "lc_expert";
    }

    @Override
    public String getTabLabel() {
        return "page_users_tab_lc_expert_label";
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new ExpertView((UsersPresenter) actions);
    }
}