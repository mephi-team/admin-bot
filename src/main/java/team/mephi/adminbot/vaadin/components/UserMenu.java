package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;

public class UserMenu extends HorizontalLayout {
    public UserMenu(AuthenticationContext authenticationContext) {
        Button logout = new IconButton(VaadinIcon.SIGN_OUT.create(), event -> authenticationContext.logout());
        add(logout);
        setPadding(true);
    }
}
