package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.CandidateDataProvider;
import team.mephi.adminbot.vaadin.users.views.CandidateView;

@SpringComponent
public class CandidateTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return 1;
    }

    @Override
    public String getTabId() {
        return "candidate";
    }

    @Override
    public String getTabLabel() {
        return "page_users_tab_candidate_label";
    }

    @Override
    public Component createTabContent(CRUDDataProvider<?> dataProvider, CRUDActions actions) {
        return new CandidateView((CandidateDataProvider) dataProvider, (UserActions) actions);
    }
}