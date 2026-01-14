package team.mephi.adminbot.vaadin.analytics.components;

public enum ActivityType {
    VISITS("visits", "page_analytics_form_activity_type_visits"),
    POPULAR_BUTTONS("popular_buttons", "page_analytics_form_activity_type_popular_buttons");

    private final String id;
    private final String labelKey;

    ActivityType(String id, String labelKey) {
        this.id = id;
        this.labelKey = labelKey;
    }

    public String getId() {
        return id;
    }

    public String getLabelKey() {
        return labelKey;
    }

    @Override
    public String toString() {
        return id;
    }
}
