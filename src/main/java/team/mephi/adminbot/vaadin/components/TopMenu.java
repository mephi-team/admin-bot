package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.vaadin.views.Analytics;
import team.mephi.adminbot.vaadin.views.Dialogs;
import team.mephi.adminbot.vaadin.views.Users;

/**
 * Верхнее меню навигации.
 */
public class TopMenu extends HorizontalLayout {
    public TopMenu(AuthService authService) {
        getElement().getStyle().set("gap", "64px");

        addClassNames(LumoUtility.JustifyContent.CENTER,
                LumoUtility.Gap.SMALL, LumoUtility.Height.MEDIUM,
                LumoUtility.Width.FULL);

        if (authService.isAdmin()) {
            add(
                    createLink(getTranslation("top_menu_communication_link"), Dialogs.class),
                    createLink(getTranslation("top_menu_users_link"), Users.class),
                    createLink(getTranslation("top_menu_analytics_link"), Analytics.class)
            );
        }

        setSizeFull();

        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    private RouterLink createLink(String text, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        var link = new RouterLink(text, navigationTarget);
        link.addClassNames(LumoUtility.TextColor.BODY);
        return link;
    }
}
