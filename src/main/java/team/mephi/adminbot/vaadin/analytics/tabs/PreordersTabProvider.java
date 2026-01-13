package team.mephi.adminbot.vaadin.analytics.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.analytics.views.PreordersView;

import static team.mephi.adminbot.vaadin.analytics.tabs.AnalyticsTabType.PREORDERS;

@SpringComponent
public class PreordersTabProvider implements AnalyticsTabProvider {
    @Override
    public AnalyticsTabType getTabId() {
        return PREORDERS;
    }

    @Override
    public String getTabLabel() {
        return PREORDERS.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new PreordersView();
    }

    @Override
    public Integer getPosition() {
        return PREORDERS.ordinal();
    }
}
