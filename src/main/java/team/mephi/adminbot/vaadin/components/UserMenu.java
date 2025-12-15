package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;

public class UserMenu extends HorizontalLayout {
    public UserMenu(AuthenticationContext authenticationContext) {
        Button logout = new Button(new Icon(VaadinIcon.SIGN_OUT), event -> authenticationContext.logout());
        add(logout);
        setPadding(true);
    }
}
