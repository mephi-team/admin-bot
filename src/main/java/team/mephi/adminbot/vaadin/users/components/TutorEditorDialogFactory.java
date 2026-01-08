package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.CityService;
import team.mephi.adminbot.service.CohortService;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.RoleService;

@SpringComponent
public class TutorEditorDialogFactory {

    private final RoleService roleService;
    private final CohortService cohortService;
    private final DirectionService directionService;
    private final CityService cityService;

    public TutorEditorDialogFactory(RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService) {
        this.roleService = roleService;
        this.cohortService = cohortService;
        this.directionService = directionService;
        this.cityService = cityService;
    }

    public TutorEditorDialog create() {
        return new TutorEditorDialog(roleService, cohortService, directionService, cityService);
    }
}
