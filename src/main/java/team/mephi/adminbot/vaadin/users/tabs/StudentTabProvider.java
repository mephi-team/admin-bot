package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.StudentDataProvider;
import team.mephi.adminbot.vaadin.users.views.StudentView;

@SpringComponent
public class StudentTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return 3;
    }

    @Override
    public String getTabId() {
        return "student";
    }

    @Override
    public String getTabLabel() {
        return "Студенты";
    }

    @Override
    public Component createTabContent(CRUDDataProvider<?> dataProvider, UserActions actions) {
        return new StudentView((StudentDataProvider) dataProvider, actions);
    }
}