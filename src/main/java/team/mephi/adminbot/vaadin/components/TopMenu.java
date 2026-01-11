package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.vaadin.views.Analytics;
import team.mephi.adminbot.vaadin.views.Dialogs;
import team.mephi.adminbot.vaadin.views.Users;

import java.util.Collection;

public class TopMenu extends HorizontalLayout {
    public TopMenu(AuthenticationContext authContext) {
        getElement().getStyle().set("gap", "64px");
        Collection<String> userRoles = authContext.getGrantedRoles();

        addClassNames(LumoUtility.JustifyContent.CENTER,
                LumoUtility.Gap.SMALL, LumoUtility.Height.MEDIUM,
                LumoUtility.Width.FULL);

        add(
                createLink(getTranslation("top_menu_communication_link"), Dialogs.class),
                createLink(getTranslation("top_menu_users_link"), Users.class)
        );

        if (userRoles.contains("ADMIN"))
            add(createLink(getTranslation("top_menu_analytics_link"), Analytics.class));

        setSizeFull();

        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    private RouterLink createLink(String text, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        var link = new RouterLink(text, navigationTarget);
        link.addClassNames(LumoUtility.TextColor.BODY);
        return link;
    }
}
