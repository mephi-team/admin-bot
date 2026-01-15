package team.mephi.adminbot.vaadin.users.tabs;

import team.mephi.adminbot.vaadin.core.CRUDActions;
import team.mephi.adminbot.vaadin.core.TabProvider;

/**
 * Провайдер вкладки для пользователей.
 */
public interface UserTabProvider extends TabProvider<CRUDActions<?>, UserTabType> {
}