package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.spring.security.AuthenticationContext;
import team.mephi.adminbot.vaadin.views.Dialogs;
import team.mephi.adminbot.vaadin.views.Mailings;
import team.mephi.adminbot.vaadin.views.Questions;

import java.util.Collection;

public class LeftMenu extends SideNav {
    public LeftMenu(AuthenticationContext authContext) {
        Collection<String> userRoles = authContext.getGrantedRoles();

        if (userRoles.contains("ADMIN")) {
            var item = new SideNavItem(getTranslation("left_menu_dialogs_link"), Dialogs.class, VaadinIcon.DASHBOARD.create());
            item.setMatchNested(true);
            addItem(item, new SideNavItem(getTranslation("left_menu_mailing_link"), Mailings.class, VaadinIcon.CART.create()));
        }
        addItem(new SideNavItem(getTranslation("left_menu_question_link"), Questions.class, VaadinIcon.USER_HEART.create()));
    }
}
