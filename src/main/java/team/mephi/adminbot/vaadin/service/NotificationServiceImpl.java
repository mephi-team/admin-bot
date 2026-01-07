package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void showNotification(NotificationType type, String message, Object... params) {
        var notification = Notification.show(I18NProvider.translate("notification_" + message, params), 3000, Notification.Position.TOP_END);
        switch (type) {
            case NotificationType.EDIT -> notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
            case NotificationType.DELETE -> notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
            case NotificationType.NEW -> notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
    }
}
