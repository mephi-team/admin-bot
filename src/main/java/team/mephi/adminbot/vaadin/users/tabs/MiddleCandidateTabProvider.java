package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.UsersPresenter;
import team.mephi.adminbot.vaadin.users.views.MiddleCandidateView;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.MIDDLE_CANDIDATE;

/**
 * Провайдер вкладки для миддл-кандидатов.
 */
@SpringComponent
public class MiddleCandidateTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return MIDDLE_CANDIDATE.ordinal();
    }

    @Override
    public UserTabType getTabId() {
        return MIDDLE_CANDIDATE;
    }

    @Override
    public String getTabLabel() {
        return MIDDLE_CANDIDATE.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new MiddleCandidateView((UsersPresenter) actions);
    }
}