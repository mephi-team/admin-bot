package team.mephi.adminbot.vaadin.service;

/**
 * Сервис для отображения уведомлений различных типов.
 */
public interface NotificationService {
    void showNotification(NotificationType type, String message, Object... params);
}
