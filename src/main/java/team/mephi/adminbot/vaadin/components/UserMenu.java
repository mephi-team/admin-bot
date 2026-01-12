package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.security.oauth2.core.oidc.StandardClaimAccessor;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.vaadin.components.buttons.IconButton;

import java.util.Objects;

public class UserMenu extends HorizontalLayout {
    public UserMenu(AuthService authService) {
        addClassNames(LumoUtility.AlignItems.CENTER);
        setPadding(true);

        StandardClaimAccessor userInfo = authService.getUserInfo();
        String fullName = userInfo.getFullName();
        String email = userInfo.getEmail();
        Span userName = new Span(Objects.isNull(fullName) ? email : fullName);
        userName.addClassNames(LumoUtility.Whitespace.NOWRAP);

        Button logout = new IconButton(VaadinIcon.SIGN_OUT.create(), event -> authService.logout());

        add(new IconButton(VaadinIcon.COG_O.create()), new Span("|"), userName, logout);
    }
}
