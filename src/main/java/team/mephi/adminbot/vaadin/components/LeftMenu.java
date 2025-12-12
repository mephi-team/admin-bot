package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import team.mephi.adminbot.vaadin.views.Dialogs;
import team.mephi.adminbot.vaadin.views.Mailings;
import team.mephi.adminbot.vaadin.views.Questions;

public class LeftMenu extends SideNav {
    public LeftMenu() {
        var item = new SideNavItem("Диалоги", Dialogs.class, VaadinIcon.DASHBOARD.create());
        item.setMatchNested(true);
        addItem(item,
                new SideNavItem("Рассылки", Mailings.class, VaadinIcon.CART.create()),
                new SideNavItem("Вопросы", Questions.class, VaadinIcon.USER_HEART.create())
        );
    }
}
