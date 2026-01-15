package team.mephi.adminbot.vaadin.analytics.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartActions;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenter;
import team.mephi.adminbot.vaadin.analytics.views.ActivityView;

import static team.mephi.adminbot.vaadin.analytics.tabs.AnalyticsTabType.ACTIVITY;

/**
 * Провайдер вкладки "Активность" в разделе аналитики.
 */
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
    @SuppressWarnings("unchecked")
    public Component createTabContent(ChartActions<?> actions) {
        return new ActivityView((ChartPresenter<ActivityView.ActivityFilterData>) actions);
    }

    @Override
    public Integer getPosition() {
        return ACTIVITY.ordinal();
    }
}
