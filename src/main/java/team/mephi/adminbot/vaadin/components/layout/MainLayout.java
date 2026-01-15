package team.mephi.adminbot.vaadin.components.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.Layout;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.vaadin.components.Logo;
import team.mephi.adminbot.vaadin.components.TopMenu;
import team.mephi.adminbot.vaadin.components.UserMenu;

/**
 * Основной макет приложения с навигацией и меню.
 */
@Layout
@PermitAll
@SuppressWarnings("unused")
public class MainLayout extends AppLayout {
    /**
     * Конструктор для создания основного макета MainLayout.
     *
     * @param authService сервис аутентификации для управления доступом пользователей.
     */
    public MainLayout(AuthService authService) {
        addToNavbar(new Logo("Neoflex"), new TopMenu(authService), new UserMenu(authService));
    }
}
