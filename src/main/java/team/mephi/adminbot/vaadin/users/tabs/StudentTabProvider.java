package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.StudentPresenter;
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
        return "page_users_tab_student_label";
    }

    @Override
    public Component createTabContent(CRUDActions actions) {
        return new StudentView((StudentPresenter) actions);
    }
}