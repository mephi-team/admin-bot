package team.mephi.adminbot.vaadin.components.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.components.buttons.MenuButton;

@PermitAll
@StyleSheet(value = "/css/app-layout-styles.css")
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
