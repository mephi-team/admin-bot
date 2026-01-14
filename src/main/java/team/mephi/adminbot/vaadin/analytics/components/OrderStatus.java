package team.mephi.adminbot.vaadin.analytics.components;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ALL("page_analytics_form_activity_status_all"),
    ACTIVE("page_analytics_form_activity_status_active"),
    REVOKED("page_analytics_form_activity_status_revoked");

    private final String translationKey;

    OrderStatus(String translationKey) {
        this.translationKey = translationKey;
    }

}
