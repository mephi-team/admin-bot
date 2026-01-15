package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.MIDDLE_CANDIDATE;

/**
 * Провайдер данных для пользователей с ролью "Миддл-кандидат".
 */
public class MiddleCandidateDataProvider extends BaseUserDataProvider {

    public MiddleCandidateDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return MIDDLE_CANDIDATE.name();
    }
}