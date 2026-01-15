package team.mephi.adminbot.vaadin.analytics.components;

import lombok.Getter;

/**
 * Статусы заказов для выбора на странице аналитики.
 */
@Getter
public enum OrderStatus {
    ALL("page_analytics_form_activity_status_all"),
    ACTIVE("page_analytics_form_activity_status_active"),
    REVOKED("page_analytics_form_activity_status_revoked");

    private final String translationKey;

    /**
     * Конструктор для статуса заказа.
     * @param translationKey Ключ для локализации метки статуса заказа.
     */
    OrderStatus(String translationKey) {
        this.translationKey = translationKey;
    }

}
