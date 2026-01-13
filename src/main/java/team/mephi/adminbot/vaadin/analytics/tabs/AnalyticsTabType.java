package team.mephi.adminbot.vaadin.analytics.tabs;

import lombok.Getter;

public enum AnalyticsTabType {
    ACTIVITY,
    PREORDERS,
    ORDERS,
    UTM;

    @Getter
    private final String tabLabelKey = "page_analytics_tab_" + name().toLowerCase() + "_label";
}
