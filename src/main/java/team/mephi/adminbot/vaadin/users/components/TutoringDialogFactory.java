package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.service.DirectionService;
import team.mephi.adminbot.service.UserService;

/**
 * Фабрика для создания экземпляров TutoringDialog с необходимыми зависимостями.
 */
@SpringComponent
public class TutoringDialogFactory {
    private final UserService userService;
    private final DirectionService directionService;

    public TutoringDialogFactory(UserService userService, DirectionService directionService) {
        this.userService = userService;
        this.directionService = directionService;
    }

    public TutoringDialog create() {
        return new TutoringDialog(userService, directionService);
    }
}
