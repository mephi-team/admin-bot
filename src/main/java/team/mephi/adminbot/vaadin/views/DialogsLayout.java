package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.vaadin.components.*;

@PermitAll
@StyleSheet(value = "/css/app-layout-styles.css")
public class DialogsLayout extends AppLayout {
    private boolean minimized = false;

    public DialogsLayout(AuthenticationContext authenticationContext) {
        addToNavbar(new Logo("Neoflex"), new TopMenu(authenticationContext), new UserMenu(authenticationContext));
        addToDrawer(new MenuButton(event -> toggleDrawerMode()), new LeftMenu());
        setDrawerMinimized(false);
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
