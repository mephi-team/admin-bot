package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.core.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.UsersPresenter;
import team.mephi.adminbot.vaadin.users.views.ExpertView;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.LC_EXPERT;

/**
 * Провайдер вкладки для экспертов.
 */
@SpringComponent
public class ExpertTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return LC_EXPERT.ordinal();
    }

    @Override
    public UserTabType getTabId() {
        return LC_EXPERT;
    }

    @Override
    public String getTabLabel() {
        return LC_EXPERT.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new ExpertView((UsersPresenter) actions);
    }
}