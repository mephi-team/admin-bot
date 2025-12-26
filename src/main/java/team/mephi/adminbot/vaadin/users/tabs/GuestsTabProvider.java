package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.GuestsDataProvider;
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
        return "Гости";
    }

    @Override
    public Component createTabContent(CRUDDataProvider<?> dataProvider, UserActions actions) {
        return new GuestsView((GuestsDataProvider) dataProvider, actions);
    }
}