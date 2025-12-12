package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.UserRepository;

@Route("/users")
public class Users extends VerticalLayout {
    public Users(UserRepository userRepository) {
        add(new H1("Пользователи"));

        Grid<User> grid = new Grid<>(User.class, false);
        grid.addColumn(User::getId).setHeader("Id").setSortable(true);
        grid.addColumn(User::getName).setHeader("Name").setSortable(true);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);

        grid.setItems(userRepository.findAllWithRoles());

        add(grid);
    }
}
