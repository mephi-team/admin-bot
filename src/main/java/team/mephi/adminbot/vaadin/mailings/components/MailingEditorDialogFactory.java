package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.UserService;

@SpringComponent
public class MailingEditorDialogFactory {
    private final UserService userService;

    public MailingEditorDialogFactory(UserService userService) {
        this.userService = userService;
    }

    public MailingEditorDialog create() {
        return new MailingEditorDialog(userService);
    }
}
