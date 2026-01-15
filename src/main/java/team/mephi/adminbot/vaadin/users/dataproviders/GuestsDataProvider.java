package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.VISITOR;

/**
 * Провайдер данных для пользователей с ролью "Гость".
 */
public class GuestsDataProvider extends BaseUserDataProvider {

    public GuestsDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return VISITOR.name();
    }
}