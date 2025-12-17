package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.CandidateDataProvider;
import team.mephi.adminbot.vaadin.users.views.CandidateView;

@SpringComponent
public class CandidateTabProvider implements UserTabProvider {

    private final CandidateDataProvider dataProvider;

    public CandidateTabProvider(CandidateDataProvider dataProvider) {
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
    public Component createTabContent(UserActions actions) {
        return new CandidateView(dataProvider, actions);
    }
}