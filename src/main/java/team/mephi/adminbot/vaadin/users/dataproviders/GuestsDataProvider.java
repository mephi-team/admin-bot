package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

public class GuestsDataProvider extends BaseUserDataProvider {

    public GuestsDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "visitor";
    }
}