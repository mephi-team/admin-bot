package team.mephi.adminbot.vaadin.components.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.vaadin.components.LeftMenu;
import team.mephi.adminbot.vaadin.components.Logo;
import team.mephi.adminbot.vaadin.components.TopMenu;
import team.mephi.adminbot.vaadin.components.UserMenu;
import team.mephi.adminbot.vaadin.components.buttons.MenuButton;

/**
 * Макет приложения с навигацией, меню и левым драйвером.
 */
@PermitAll
public class DialogsLayout extends AppLayout {
    private boolean minimized = true;

    public DialogsLayout(AuthService authService) {
        addToNavbar(new Logo("Neoflex"), new TopMenu(authService), new UserMenu(authService));
        addToDrawer(new MenuButton(event -> toggleDrawerMode()), new LeftMenu(authService));
        setDrawerMinimized(true);
    }

    private void toggleDrawerMode() {
        minimized = !minimized;
        setDrawerMinimized(minimized);
    }

    private void setDrawerMinimized(boolean minimize) {
        if (minimize) {
            getElement().setAttribute("mini-variant", true);
        } else {
            getElement().removeAttribute("mini-variant");
        }
        // Здесь можно также менять иконку кнопки minimizeButton
    }
}
