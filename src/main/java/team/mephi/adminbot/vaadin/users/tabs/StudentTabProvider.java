package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.core.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.StudentPresenter;
import team.mephi.adminbot.vaadin.users.views.StudentView;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.STUDENT;

/**
 * Провайдер вкладки для студентов.
 */
@SpringComponent
public class StudentTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return STUDENT.ordinal();
    }

    @Override
    public UserTabType getTabId() {
        return STUDENT;
    }

    @Override
    public String getTabLabel() {
        return STUDENT.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new StudentView((StudentPresenter) actions);
    }
}