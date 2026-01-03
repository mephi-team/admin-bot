package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

public class FreeListenerDataProvider extends BaseUserDataProvider {

    public FreeListenerDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "free_listener";
    }
}