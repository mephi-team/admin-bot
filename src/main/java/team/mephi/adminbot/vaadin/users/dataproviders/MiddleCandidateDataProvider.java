package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.vaadin.users.service.UserCountService;

public class MiddleCandidateDataProvider extends BaseUserDataProvider {

    public MiddleCandidateDataProvider(UserCountService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "middle_candidate";
    }
}