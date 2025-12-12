package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.Layout;
import team.mephi.adminbot.vaadin.components.Logo;
import team.mephi.adminbot.vaadin.components.TopMenu;

@Layout
@StyleSheet(value = "/css/app-layout-styles.css")
public class MainLayout extends AppLayout {
    public MainLayout() {
        addToNavbar(new Logo("Neoflex"), new TopMenu());
    }
}
