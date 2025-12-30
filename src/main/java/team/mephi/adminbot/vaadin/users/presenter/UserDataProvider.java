package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDDataProvider;

public interface UserDataProvider extends CRUDDataProvider<SimpleUser> {
    void blockAllById(Iterable<Long> ids);
}