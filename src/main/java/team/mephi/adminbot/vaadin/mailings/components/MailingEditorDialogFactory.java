package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.service.CityService;
import team.mephi.adminbot.service.DirectionService;

@SpringComponent
public class MailingEditorDialogFactory {
    private final UserService userService;
    private final DirectionService directionService;
    private final CityService cityService;

    public MailingEditorDialogFactory(UserService userService, DirectionService directionService, CityService cityService) {
        this.userService = userService;
        this.directionService = directionService;
        this.cityService = cityService;
    }

    public MailingEditorDialog create() {
        return new MailingEditorDialog(userService, directionService, cityService);
    }
}
