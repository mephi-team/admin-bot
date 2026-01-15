package team.mephi.adminbot.vaadin.users.tabs;

import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.TabProvider;

/**
 * Провайдер вкладки для пользователей.
 */
public interface UserTabProvider extends TabProvider<CRUDActions<?>, UserTabType> {
}