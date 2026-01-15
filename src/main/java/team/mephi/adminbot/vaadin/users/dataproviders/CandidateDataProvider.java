package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.CANDIDATE;

/**
 * Провайдер данных для пользователей с ролью "Кандидат".
 */
public class CandidateDataProvider extends BaseUserDataProvider {

    /**
     * Конструктор провайдера данных для кандидатов.
     *
     * @param userService сервис для работы с пользователями.
     */
    public CandidateDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return CANDIDATE.name();
    }
}