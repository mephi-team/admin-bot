package team.mephi.adminbot.vaadin.analytics.tabs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.analytics.views.OrdersView;

import static team.mephi.adminbot.vaadin.analytics.tabs.AnalyticsTabType.ORDERS;

@SpringComponent
public class OrdersTabProvider implements AnalyticsTabProvider {
    @Override
    public AnalyticsTabType getTabId() {
        return ORDERS;
    }

    @Override
    public String getTabLabel() {
        return ORDERS.getTabLabelKey();
    }

    @Override
    public Component createTabContent(CRUDActions<?> actions) {
        return new OrdersView();
    }

    @Override
    public Integer getPosition() {
        return ORDERS.ordinal();
    }
}
