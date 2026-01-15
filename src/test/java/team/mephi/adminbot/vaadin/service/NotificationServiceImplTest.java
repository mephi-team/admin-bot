//package team.mephi.adminbot.vaadin.service;
//
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.notification.NotificationVariant;
//import com.vaadin.flow.i18n.I18NProvider;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Locale;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//
/// **
// * Юнит-тесты для NotificationServiceImpl.
// * Покрывают: выбор вариантов оформления уведомлений.
// */
//@ExtendWith(MockitoExtension.class)
//class NotificationServiceImplTest {
//
//    /**
//     * Проверяет оформление уведомления для типа NEW.
//     */
//    @Test
//    void Given_newType_When_showNotification_Then_appliesSuccessVariant() {
//        // Arrange
//        NotificationServiceImpl service = new NotificationServiceImpl();
//        Notification notification = mock(Notification.class);
//
//        try (MockedStatic<I18NProvider> i18n = Mockito.mockStatic(I18NProvider.class);
//             MockedStatic<Notification> notificationStatic = Mockito.mockStatic(Notification.class)) {
//
//            // мок на оба overload'а translate(...)
//            i18n.when(() -> I18NProvider.translate(eq("message"), Mockito.<Object[]>any()))
//                    .thenReturn("translated");
//            i18n.when(() -> I18NProvider.translate(any(Locale.class), eq("message"), Mockito.<Object[]>any()))
//                    .thenReturn("translated");
//
//            notificationStatic.when(() ->
//                            Notification.show(anyString(), eq(3000), eq(Notification.Position.TOP_END)))
//                    .thenReturn(notification);
//
//            // Act
//            service.showNotification(NotificationType.NEW, "message", "param");
//
//            // Assert
//            verify(notification).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//        }
//    }
//
//    /**
//     * Проверяет оформление уведомления для типа EDIT.
//     */
//    @Test
//    void Given_editType_When_showNotification_Then_appliesWarningVariant() {
//        // Arrange
//        NotificationServiceImpl service = new NotificationServiceImpl();
//        Notification notification = mock(Notification.class);
//
//        try (MockedStatic<I18NProvider> i18n = Mockito.mockStatic(I18NProvider.class);
//             MockedStatic<Notification> notificationStatic = Mockito.mockStatic(Notification.class)) {
//
//            i18n.when(() -> I18NProvider.translate(eq("edit"), Mockito.<Object[]>any()))
//                    .thenReturn("translated");
//            i18n.when(() -> I18NProvider.translate(any(Locale.class), eq("edit"), Mockito.<Object[]>any()))
//                    .thenReturn("translated");
//
//            notificationStatic.when(() ->
//                            Notification.show(anyString(), eq(3000), eq(Notification.Position.TOP_END)))
//                    .thenReturn(notification);
//
//            // Act
//            service.showNotification(NotificationType.EDIT, "edit");
//
//            // Assert
//            verify(notification).addThemeVariants(NotificationVariant.LUMO_WARNING);
//        }
//    }
//
//    /**
//     * Проверяет оформление уведомления для типа DELETE.
//     */
//    @Test
//    void Given_deleteType_When_showNotification_Then_appliesContrastVariant() {
//        // Arrange
//        NotificationServiceImpl service = new NotificationServiceImpl();
//        Notification notification = mock(Notification.class);
//
//        try (MockedStatic<I18NProvider> i18n = Mockito.mockStatic(I18NProvider.class);
//             MockedStatic<Notification> notificationStatic = Mockito.mockStatic(Notification.class)) {
//
//            i18n.when(() -> I18NProvider.translate(eq("delete"), Mockito.<Object[]>any()))
//                    .thenReturn("translated");
//            i18n.when(() -> I18NProvider.translate(any(Locale.class), eq("delete"), Mockito.<Object[]>any()))
//                    .thenReturn("translated");
//
//            notificationStatic.when(() ->
//                            Notification.show(anyString(), eq(3000), eq(Notification.Position.TOP_END)))
//                    .thenReturn(notification);
//
//            // Act
//            service.showNotification(NotificationType.DELETE, "delete");
//
//            // Assert
//            verify(notification).addThemeVariants(NotificationVariant.LUMO_CONTRAST);
//        }
//    }
//}
