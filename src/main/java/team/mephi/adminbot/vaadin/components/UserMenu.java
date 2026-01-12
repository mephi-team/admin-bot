package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Objects;

public class UserMenu extends HorizontalLayout {
    public UserMenu(AuthenticationContext authenticationContext) {
        addClassNames(LumoUtility.AlignItems.CENTER);
        setPadding(true);

        DefaultOidcUser user = authenticationContext.getAuthenticatedUser(DefaultOidcUser.class).orElseThrow();
        String fullName = user.getUserInfo().getFullName();
        String email = user.getUserInfo().getEmail();
        Span userName = new Span(Objects.isNull(fullName) ? email : fullName);
        userName.addClassNames(LumoUtility.Whitespace.NOWRAP);

        Button logout = new IconButton(VaadinIcon.SIGN_OUT.create(), event -> authenticationContext.logout());

        add(userName, logout);
    }
}
