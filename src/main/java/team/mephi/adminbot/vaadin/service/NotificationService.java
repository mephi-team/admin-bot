package team.mephi.adminbot.vaadin.service;

public interface NotificationService {
    void showNotification(NotificationType type, String message, Object ... params);
}
