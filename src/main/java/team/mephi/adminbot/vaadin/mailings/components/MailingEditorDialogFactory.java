package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.*;

/**
 * Фабрика для создания экземпляров MailingEditorDialog с внедрением зависимостей.
 */
@SpringComponent
public class MailingEditorDialogFactory {
    private final UserService userService;
    private final RoleService roleService;
    private final CohortService cohortService;
    private final DirectionService directionService;
    private final CityService cityService;
    private final TemplateService templateService;

    /**
     * Конструктор фабрики с внедрением необходимых сервисов.
     *
     * @param userService      сервис для работы с пользователями
     * @param roleService      сервис для работы с ролями
     * @param cohortService    сервис для работы с когортами
     * @param directionService сервис для работы с направлениями
     * @param cityService      сервис для работы с городами
     * @param templateService  сервис для работы с шаблонами
     */
    public MailingEditorDialogFactory(UserService userService, RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService, TemplateService templateService) {
        this.userService = userService;
        this.roleService = roleService;
        this.cohortService = cohortService;
        this.directionService = directionService;
        this.cityService = cityService;
        this.templateService = templateService;
    }

    /**
     * Создает новый экземпляр MailingEditorDialog с внедренными зависимостями.
     *
     * @return новый экземпляр MailingEditorDialog
     */
    public MailingEditorDialog create() {
        return new MailingEditorDialog(userService, roleService, cohortService, directionService, cityService, templateService);
    }
}
