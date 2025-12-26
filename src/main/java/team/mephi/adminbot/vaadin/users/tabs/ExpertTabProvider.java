package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.ExpertDataProvider;
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
    public Component createTabContent(CRUDDataProvider<?> dataProvider, CRUDActions actions) {
        return new ExpertView((ExpertDataProvider) dataProvider, (UserActions) actions);
    }
}