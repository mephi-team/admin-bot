package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.vaadin.views.Dialogs;
import team.mephi.adminbot.vaadin.views.Mailings;
import team.mephi.adminbot.vaadin.views.Questions;

/**
 * Левое меню навигации.
 */
public class LeftMenu extends SideNav {
    /**
     * Создает левое меню с элементами навигации в зависимости от прав пользователя.
     *
     * @param authService Сервис аутентификации для проверки прав пользователя.
     */
    public LeftMenu(AuthService authService) {

        if (authService.isAdmin()) {
            var item = new SideNavItem(getTranslation("left_menu_dialogs_link"), Dialogs.class, VaadinIcon.DASHBOARD.create());
            item.setMatchNested(true);
            addItem(item, new SideNavItem(getTranslation("left_menu_mailing_link"), Mailings.class, VaadinIcon.CART.create()));
        }
        addItem(new SideNavItem(getTranslation("left_menu_question_link"), Questions.class, VaadinIcon.USER_HEART.create()));
    }
}
