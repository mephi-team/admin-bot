package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.*;

@SpringComponent
public class MailingEditorDialogFactory {
    private final UserService userService;
    private final RoleService roleService;
    private final CohortService cohortService;
    private final DirectionService directionService;
    private final CityService cityService;
    private final TemplateService templateService;

    public MailingEditorDialogFactory(UserService userService, RoleService roleService, CohortService cohortService, DirectionService directionService, CityService cityService, TemplateService templateService) {
        this.userService = userService;
        this.roleService = roleService;
        this.cohortService = cohortService;
        this.directionService = directionService;
        this.cityService = cityService;
        this.templateService = templateService;
    }

    public MailingEditorDialog create() {
        return new MailingEditorDialog(userService, roleService, cohortService, directionService, cityService, templateService);
    }
}
