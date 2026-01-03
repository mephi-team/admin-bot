package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

public class CandidateDataProvider extends BaseUserDataProvider {

    public CandidateDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "candidate";
    }
}