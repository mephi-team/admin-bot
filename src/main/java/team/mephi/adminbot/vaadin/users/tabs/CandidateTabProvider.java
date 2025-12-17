package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.CandidateDataProvider;
import team.mephi.adminbot.vaadin.users.dataproviders.GuestsDataProvider;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;
import team.mephi.adminbot.vaadin.users.views.CandidateView;
import team.mephi.adminbot.vaadin.users.views.GuestsView;

@SpringComponent
public class CandidateTabProvider implements UserTabProvider {

    private final GuestsDataProvider dataProvider;

    public CandidateTabProvider(GuestsDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

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
        return "Кандидаты";
    }

    @Override
    public Component createTabContent(UserDataProvider provider, UserActions actions) {
        return new CandidateView((CandidateDataProvider) provider, actions);
    }
}