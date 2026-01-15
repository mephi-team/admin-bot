package team.mephi.adminbot.vaadin.analytics.components;

import lombok.Getter;

/**
 * Типы активности для выбора на странице аналитики.
 */
@Getter
public enum ActivityType {
    VISITS("visits", "page_analytics_form_activity_type_visits"),
    POPULAR_BUTTONS("popular_buttons", "page_analytics_form_activity_type_popular_buttons");

    private final String id;
    private final String labelKey;

    ActivityType(String id, String labelKey) {
        this.id = id;
        this.labelKey = labelKey;
    }

    @Override
    public String toString() {
        return id;
    }
}
