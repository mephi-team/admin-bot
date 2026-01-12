package team.mephi.adminbot.vaadin.components.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.vaadin.components.Logo;
import team.mephi.adminbot.vaadin.components.TopMenu;
import team.mephi.adminbot.vaadin.components.UserMenu;

@Layout
@PermitAll
@StyleSheet(value = "/css/app-layout-styles.css")
public class MainLayout extends AppLayout {
    public MainLayout(AuthenticationContext authenticationContext) {
        addToNavbar(new Logo("Neoflex"), new TopMenu(authenticationContext), new UserMenu(authenticationContext));
    }
}
