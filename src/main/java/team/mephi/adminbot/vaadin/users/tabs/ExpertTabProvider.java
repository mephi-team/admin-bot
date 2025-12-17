package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.ExpertDataProvider;
import team.mephi.adminbot.vaadin.users.views.ExpertView;

@SpringComponent
public class ExpertTabProvider implements UserTabProvider {

    private final ExpertDataProvider dataProvider;

    public ExpertTabProvider(ExpertDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Integer getPosition() {
        return 5;
    }

    @Override
    public String getTabId() {
        return "lc_expert";
    }

    @Override
    public String getTabLabel() {
        return "Эксперты";
    }

    @Override
    public Component createTabContent(UserActions actions) {
        return new ExpertView(dataProvider, actions);
    }
}