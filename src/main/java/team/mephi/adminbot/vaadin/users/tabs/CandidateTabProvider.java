package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.UsersPresenter;
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
    public Component createTabContent(CRUDActions<?> actions) {
        return new CandidateView((UsersPresenter) actions);
    }
}