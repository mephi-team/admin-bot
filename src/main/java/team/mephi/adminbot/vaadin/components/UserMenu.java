package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

public class UserMenu extends HorizontalLayout {
    public UserMenu(AuthenticationContext authenticationContext) {
        addClassNames(LumoUtility.AlignItems.CENTER);
        setPadding(true);

        DefaultOidcUser user = authenticationContext.getAuthenticatedUser(DefaultOidcUser.class).orElseThrow();
        Span userName = new Span(user.getUserInfo().getFullName());
        userName.addClassNames(LumoUtility.Whitespace.NOWRAP);

        Button logout = new IconButton(VaadinIcon.SIGN_OUT.create(), event -> authenticationContext.logout());

        add(userName, logout);
    }
}
