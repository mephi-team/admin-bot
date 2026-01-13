package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.CANDIDATE;

public class CandidateDataProvider extends BaseUserDataProvider {

    public CandidateDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return CANDIDATE.name();
    }
}