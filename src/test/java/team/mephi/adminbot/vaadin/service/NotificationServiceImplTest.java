package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.i18n.I18NProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

/**
 * Тесты для {@link NotificationServiceImpl}.
 */
class NotificationServiceImplTest {
    /**
     * Проверяет добавление корректной темы в зависимости от типа уведомления.
     */
    @ParameterizedTest
    @EnumSource(NotificationType.class)
    void givenNotificationType_WhenShowNotificationCalled_ThenVariantApplied(NotificationType type) {
        // Arrange
        NotificationServiceImpl service = new NotificationServiceImpl();
        Notification notification = mock(Notification.class);

        try (MockedStatic<I18NProvider> i18n = mockStatic(I18NProvider.class);
             MockedStatic<Notification> notifications = mockStatic(Notification.class)) {
            i18n.when(() -> I18NProvider.translate(eq("message"), any())).thenReturn("message");
            notifications.when(() -> Notification.show("message", 3000, Notification.Position.TOP_END))
                    .thenReturn(notification);

            // Act
            service.showNotification(type, "message", "arg");

            // Assert
            NotificationVariant expectedVariant = switch (type) {
                case EDIT -> NotificationVariant.LUMO_WARNING;
                case DELETE -> NotificationVariant.LUMO_CONTRAST;
                case NEW -> NotificationVariant.LUMO_SUCCESS;
            };
            verify(notification).addThemeVariants(expectedVariant);
        }
    }
}
