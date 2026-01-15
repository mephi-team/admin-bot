package team.mephi.adminbot.vaadin.service;

/**
 * Сервис для отображения уведомлений различных типов.
 */
public interface NotificationService {
    /**
     * Отображает уведомление указанного типа с заданным сообщением и параметрами.
     *
     * @param type    Тип уведомления.
     * @param message Сообщение уведомления.
     * @param params  Параметры для форматирования сообщения.
     */
    void showNotification(NotificationType type, String message, Object... params);
}
