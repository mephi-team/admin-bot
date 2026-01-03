package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.service.CityService;

@SpringComponent
public class MailingEditorDialogFactory {
    private final UserService userService;
    private final CityService cityService;

    public MailingEditorDialogFactory(UserService userService, CityService cityService) {
        this.userService = userService;
        this.cityService = cityService;
    }

    public MailingEditorDialog create() {
        return new MailingEditorDialog(userService, cityService);
    }
}
