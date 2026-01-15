package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.core.CRUDActions;

/**
 * Интерфейс для действий, связанных с пользователями.
 */
public interface UserActions extends CRUDActions<SimpleUser>, BlockingActions<SimpleUser>, AcceptableActions {
}