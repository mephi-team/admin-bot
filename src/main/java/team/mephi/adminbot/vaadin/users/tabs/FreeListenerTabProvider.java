package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.StudentPresenter;
import team.mephi.adminbot.vaadin.users.views.FreeListenerView;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.FREE_LISTENER;

/**
 * Провайдер вкладки для слушателей.
 */
@SpringComponent
public class FreeListenerTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return FREE_LISTENER.ordinal();
    }

    @Override
    public UserTabType getTabId() {
        return FREE_LISTENER;
    }

    @Override
    public String getTabLabel() {
        return FREE_LISTENER.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new FreeListenerView((StudentPresenter) actions);
    }
}