package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.vaadin.users.service.UserCountService;

public class ExpertDataProvider extends BaseUserDataProvider {

    public ExpertDataProvider(UserCountService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "lc_expert";
    }
}