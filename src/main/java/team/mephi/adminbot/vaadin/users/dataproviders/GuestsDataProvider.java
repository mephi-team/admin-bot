package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.vaadin.users.service.UserCountService;

public class GuestsDataProvider extends BaseUserDataProvider {
    public GuestsDataProvider(UserCountService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "visitor";
    }
}