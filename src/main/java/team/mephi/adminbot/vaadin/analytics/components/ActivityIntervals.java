package team.mephi.adminbot.vaadin.analytics.components;

import lombok.Getter;

public enum ActivityIntervals {
    MONTH,
    DAY,
    HOUR;

    @Getter
    private final String tabLabelKey = "page_analytics_form1_interval_" + name().toLowerCase() + "_label";
}
