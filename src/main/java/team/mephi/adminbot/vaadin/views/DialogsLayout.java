package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import team.mephi.adminbot.vaadin.components.LeftMenu;
import team.mephi.adminbot.vaadin.components.Logo;
import team.mephi.adminbot.vaadin.components.MenuButton;
import team.mephi.adminbot.vaadin.components.TopMenu;

@StyleSheet(value = "/css/app-layout-styles.css")
public class DialogsLayout extends AppLayout {
    private boolean minimized = false;

    public DialogsLayout() {
        addToNavbar(new Logo("Neoflex1"), new TopMenu());
        addToDrawer(new MenuButton(event -> toggleDrawerMode()), new LeftMenu());
        setDrawerMinimized(false);
        getElement().getStyle().set("height", "100%");
//        getElement().getStyle().set("border", "3px solid green");
//        getElement().getStyle().set("overflow", "hidden");

//        getContent().getElement().getStyle().set("border", "3px solid green");
//        getContent().getElement().getStyle().set("border", "1px solid blue");
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
