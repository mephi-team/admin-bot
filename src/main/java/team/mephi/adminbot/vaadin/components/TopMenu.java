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
        Collection<String> userRoles = authContext.getGrantedRoles();

        addClassNames(LumoUtility.JustifyContent.CENTER,
                LumoUtility.Gap.SMALL, LumoUtility.Height.MEDIUM,
                LumoUtility.Width.FULL);

        add(
                new RouterLink("Общение", Dialogs.class),
                new RouterLink("Пользователи", Users.class)
        );

        if (userRoles.contains("ADMIN"))
            add(new RouterLink("Аналитика", Analytics.class));

        setSizeFull();

        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

}
