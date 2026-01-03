package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

public class MiddleCandidateDataProvider extends BaseUserDataProvider {

    public MiddleCandidateDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return "middle_candidate";
    }
}