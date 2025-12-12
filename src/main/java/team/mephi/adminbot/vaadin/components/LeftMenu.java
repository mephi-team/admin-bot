package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class LeftMenu extends SideNav {
    public LeftMenu() {
        addItem(
                new SideNavItem("Диалоги", "/dialogs",
                        VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Рассылки", "/mailings",
                        VaadinIcon.CART.create()),
                new SideNavItem("Вопросы", "/questions",
                        VaadinIcon.USER_HEART.create())
        );
    }
}
