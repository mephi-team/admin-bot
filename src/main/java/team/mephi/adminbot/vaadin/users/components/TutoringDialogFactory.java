package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.UserService;

@SpringComponent
public class TutoringDialogFactory {
    private final UserService userService;

    public TutoringDialogFactory(UserService userService) {
        this.userService = userService;
    }

    public TutoringDialog create() {
        return new TutoringDialog(userService);
    }
}
