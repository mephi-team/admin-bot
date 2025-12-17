package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.TutorDataProvider;
import team.mephi.adminbot.vaadin.users.views.TutorView;

@SpringComponent
public class TutorTabProvider implements UserTabProvider {

    private final TutorDataProvider dataProvider;

    public TutorTabProvider(TutorDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Integer getPosition() {
        return 6;
    }

    @Override
    public String getTabId() {
        return "tutor";
    }

    @Override
    public String getTabLabel() {
        return "Кураторы";
    }

    @Override
    public Component createTabContent(UserActions actions) {
        return new TutorView(dataProvider, actions);
    }
}