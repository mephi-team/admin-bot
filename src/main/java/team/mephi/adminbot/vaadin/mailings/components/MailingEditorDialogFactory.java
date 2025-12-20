package team.mephi.adminbot.vaadin.mailings.components;

import com.vaadin.flow.spring.annotation.SpringComponent;

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
