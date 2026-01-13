package team.mephi.adminbot.vaadin.analytics.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartActions;
import team.mephi.adminbot.vaadin.analytics.views.UtmView;

import static team.mephi.adminbot.vaadin.analytics.tabs.AnalyticsTabType.UTM;

@SpringComponent
public class UtmTabProvider implements AnalyticsTabProvider {
    @Override
    public AnalyticsTabType getTabId() {
        return UTM;
    }

    @Override
    public String getTabLabel() {
        return UTM.getTabLabelKey();
    }

    @Override
    public Component createTabContent(ChartActions<?> actions) {
        return new UtmView();
    }

    @Override
    public Integer getPosition() {
        return UTM.ordinal();
    }
}
