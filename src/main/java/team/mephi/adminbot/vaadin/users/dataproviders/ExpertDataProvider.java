package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

public class ExpertDataProvider extends BaseUserDataProvider {

    public ExpertDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "lc_expert";
    }
}