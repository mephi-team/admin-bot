package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.LC_EXPERT;

public class ExpertDataProvider extends BaseUserDataProvider {

    public ExpertDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return LC_EXPERT.name();
    }
}