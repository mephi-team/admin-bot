package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.UsersPresenter;
import team.mephi.adminbot.vaadin.users.views.CandidateView;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.CANDIDATE;

/**
 * Провайдер вкладки для кандидатов в пользователи.
 */
@SpringComponent
public class CandidateTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return CANDIDATE.ordinal();
    }

    @Override
    public UserTabType getTabId() {
        return CANDIDATE;
    }

    @Override
    public String getTabLabel() {
        return CANDIDATE.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new CandidateView((UsersPresenter) actions);
    }
}