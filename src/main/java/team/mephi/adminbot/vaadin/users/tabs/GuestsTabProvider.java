package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.core.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.BlockingPresenter;
import team.mephi.adminbot.vaadin.users.views.GuestsView;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.VISITOR;

/**
 * Провайдер вкладки для гостей.
 */
@SpringComponent
public class GuestsTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return VISITOR.ordinal();
    }

    @Override
    public UserTabType getTabId() {
        return VISITOR;
    }

    @Override
    public String getTabLabel() {
        return VISITOR.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new GuestsView((BlockingPresenter) actions);
    }
}