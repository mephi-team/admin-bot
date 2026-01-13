package team.mephi.adminbot.vaadin.analytics.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.analytics.views.ActivityView;

import static team.mephi.adminbot.vaadin.analytics.tabs.AnalyticsTabType.ACTIVITY;

@SpringComponent
public class ActivityTabProvider implements AnalyticsTabProvider {
    @Override
    public AnalyticsTabType getTabId() {
        return ACTIVITY;
    }

    @Override
    public String getTabLabel() {
        return ACTIVITY.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new ActivityView();
    }

    @Override
    public Integer getPosition() {
        return ACTIVITY.ordinal();
    }
}
