package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.FREE_LISTENER;

public class FreeListenerDataProvider extends BaseUserDataProvider {

    public FreeListenerDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return FREE_LISTENER.name();
    }
}