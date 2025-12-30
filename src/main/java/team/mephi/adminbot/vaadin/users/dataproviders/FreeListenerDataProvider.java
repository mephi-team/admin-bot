package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.vaadin.users.service.UserCountService;

public class FreeListenerDataProvider extends BaseUserDataProvider {

    public FreeListenerDataProvider(UserCountService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "free_listener";
    }
}