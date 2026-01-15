package team.mephi.adminbot.vaadin.users.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.users.presenter.TutorPresenter;
import team.mephi.adminbot.vaadin.users.views.TutorView;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.TUTOR;

/**
 * Провайдер вкладки для кураторов.
 */
@SpringComponent
public class TutorTabProvider implements UserTabProvider {
    @Override
    public Integer getPosition() {
        return TUTOR.ordinal();
    }

    @Override
    public UserTabType getTabId() {
        return TUTOR;
    }

    @Override
    public String getTabLabel() {
        return TUTOR.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new TutorView((TutorPresenter) actions);
    }
}