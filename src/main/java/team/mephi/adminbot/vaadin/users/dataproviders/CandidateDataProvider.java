package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.vaadin.users.service.UserCountService;

public class CandidateDataProvider extends BaseUserDataProvider {

    public CandidateDataProvider(UserCountService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "candidate";
    }
}