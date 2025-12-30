package team.mephi.adminbot.vaadin.users.actions;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDActions;

public interface UserActions extends CRUDActions<SimpleUser>, BlockingActions, AcceptableActions {
}