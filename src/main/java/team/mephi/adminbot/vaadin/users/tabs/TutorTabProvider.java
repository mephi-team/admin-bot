package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.TutorPresenter;
import team.mephi.adminbot.vaadin.users.views.TutorView;

@SpringComponent
public class TutorTabProvider implements UserTabProvider {
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
        return "page_users_tab_tutor_label";
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new TutorView((TutorPresenter) actions);
    }
}