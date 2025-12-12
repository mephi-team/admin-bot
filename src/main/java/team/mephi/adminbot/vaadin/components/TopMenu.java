package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.vaadin.views.Analytics;
import team.mephi.adminbot.vaadin.views.Dialogs;
import team.mephi.adminbot.vaadin.views.Users;

public class TopMenu extends HorizontalLayout  {
    public TopMenu() {
        addClassNames(LumoUtility.JustifyContent.CENTER,
                LumoUtility.Gap.SMALL, LumoUtility.Height.MEDIUM,
                LumoUtility.Width.FULL);
        var link = new RouterLink("Общение", Dialogs.class);
        add(
                link,
                new RouterLink("Пользователи", Users.class),
                new RouterLink("Аналитика", Analytics.class)
        );
        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

}
