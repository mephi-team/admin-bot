package team.mephi.adminbot.vaadin.users.dataproviders;

import team.mephi.adminbot.service.UserService;

import static team.mephi.adminbot.vaadin.users.tabs.UserTabType.STUDENT;

/**
 * Провайдер данных для пользователей с ролью "Студент".
 */
public class StudentDataProvider extends BaseUserDataProvider {

    /**
     * Конструктор провайдера данных для студентов.
     *
     * @param userService сервис для работы с пользователями.
     */
    public StudentDataProvider(UserService userService) {
        super(userService);
    }

    @Override
    protected String getRole() {
        return STUDENT.name();
    }
}