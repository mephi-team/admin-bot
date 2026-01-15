package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.FREE_LISTENER;

/**
 * Провайдер данных для пользователей с ролью "Слушатель".
 */
public class FreeListenerDataProvider extends BaseUserDataProvider {

    /**
     * Конструктор провайдера данных для слушателей.
     *
     * @param userService сервис для работы с пользователями.
     */
    public FreeListenerDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return FREE_LISTENER.name();
    }
}