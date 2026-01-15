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

    /**
     * Конструктор фабрики TutoringDialogFactory.
     *
     * @param userService      сервис для работы с пользователями.
     * @param directionService сервис для работы с направлениями.
     */
    public TutoringDialogFactory(UserService userService, DirectionService directionService) {
        this.userService = userService;
        this.directionService = directionService;
    }

    /**
     * Создает новый экземпляр TutoringDialog с внедренными зависимостями.
     *
     * @return новый экземпляр TutoringDialog.
     */
    public TutoringDialog create() {
        return new TutoringDialog(userService, directionService);
    }
}
