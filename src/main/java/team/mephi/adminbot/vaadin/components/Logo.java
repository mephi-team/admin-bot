package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import team.mephi.adminbot.vaadin.views.Dashboard;

/**
 * Логотип приложения с ссылкой на главную страницу.
 */
public class Logo extends HorizontalLayout {
    public Logo(String text) {
        setPadding(false);
        getElement().getStyle().set("margin-left", "40px");
        RouterLink link = new RouterLink(Dashboard.class);
        Image logo = new Image("/images/logo.svg", text);
        logo.setWidth("96px");
        link.add(logo);
        add(link);
    }
}
