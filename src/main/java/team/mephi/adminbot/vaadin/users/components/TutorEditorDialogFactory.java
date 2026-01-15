package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.RoleService;
import team.mephi.adminbot.service.UserService;

/**
 * Фабрика для создания диалогов редактирования кураторов.
 */
@SpringComponent
public class TutorEditorDialogFactory {

    private final RoleService roleService;
    private final CohortService cohortService;
    private final DirectionService directionService;
    private final UserService userService;

    /**
     * Конструктор фабрики диалогов редактирования кураторов.
     *
     * @param roleService    сервис для работы с ролями
     * @param cohortService  сервис для работы с когорты
     * @param directionService сервис для работы с направлениями
     * @param userService    сервис для работы с пользователями
     */
    public TutorEditorDialogFactory(RoleService roleService, CohortService cohortService, DirectionService directionService, UserService userService) {
        this.roleService = roleService;
        this.cohortService = cohortService;
        this.directionService = directionService;
        this.userService = userService;
    }

    /**
     * Создает диалоговое окно для редактирования куратора.
     *
     * @return созданное диалоговое окно для редактирования куратора
     */
    public TutorEditorDialog create() {
        return new TutorEditorDialog(roleService, cohortService, directionService, userService);
    }
}
