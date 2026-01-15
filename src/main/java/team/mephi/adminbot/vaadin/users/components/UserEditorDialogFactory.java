package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.*;

/**
 * Фабрика для создания экземпляров UserEditorDialog с необходимыми зависимостями.
 */
@SpringComponent
public class UserEditorDialogFactory {

    private final RoleService roleService;
    private final CohortService cohortService;
    private final DirectionService directionService;
    private final CityService cityService;
    private final TutorService tutorService;

    /**
     * Конструктор фабрики UserEditorDialogFactory.
     *
     * @param roleService      сервис для работы с ролями пользователей.
     * @param cohortService    сервис для работы с когортами.
     * @param directionService сервис для работы с направлениями.
     * @param cityService      сервис для работы с городами.
     * @param tutorService     сервис для работы с кураторами.
     */
    public UserEditorDialogFactory(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService, TutorService tutorService) {
        this.roleService = roleService;
        this.cohortService = cohortService;
        this.directionService = directionService;
        this.cityService = cityService;
        this.tutorService = tutorService;
    }

    /**
     * Создает новый экземпляр UserEditorDialog с внедренными зависимостями.
     *
     * @return новый экземпляр UserEditorDialog.
     */
    public UserEditorDialog create() {
        return new UserEditorDialog(roleService, cohortService, directionService, cityService, tutorService);
    }
}
