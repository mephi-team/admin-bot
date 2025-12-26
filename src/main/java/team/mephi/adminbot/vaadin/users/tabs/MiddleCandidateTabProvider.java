package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.MiddleCandidateDataProvider;
import team.mephi.adminbot.vaadin.users.views.MiddleCandidateView;

@SpringComponent
public class MiddleCandidateTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return 2;
    }

    @Override
    public String getTabId() {
        return "middle_candidate";
    }

    @Override
    public String getTabLabel() {
        return "Миддл-Кандидаты";
    }

    @Override
    public Component createTabContent(CRUDDataProvider<?> dataProvider, UserActions actions) {
        return new MiddleCandidateView((MiddleCandidateDataProvider) dataProvider, actions);
    }
}