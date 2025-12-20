package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class UserEditorDialogFactory {

    private final RoleService roleService;
    private final CohortService cohortService;
    private final DirectionService directionService;
    private final CityService cityService;

    public UserEditorDialogFactory(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService) {
        this.roleService = roleService;
        this.cohortService = cohortService;
        this.directionService = directionService;
        this.cityService = cityService;
    }

    public UserEditorDialog create() {
        return new UserEditorDialog(roleService, cohortService, directionService, cityService);
    }
}
