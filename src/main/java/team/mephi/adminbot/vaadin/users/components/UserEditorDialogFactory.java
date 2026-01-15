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

    public UserEditorDialogFactory(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService, TutorService tutorService) {
        this.roleService = roleService;
        this.cohortService = cohortService;
        this.directionService = directionService;
        this.cityService = cityService;
        this.tutorService = tutorService;
    }

    public UserEditorDialog create() {
        return new UserEditorDialog(roleService, cohortService, directionService, cityService, tutorService);
    }
}
