package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.components.SearchField;

public class UsersView extends VerticalLayout {
    private final String role;

    public UsersView(UserRepository userRepository, String role) {
        this.role = role;

        setHeightFull();
        final TextField searchField = new SearchField("Найти куратора");

        Grid<User> grid = new Grid<>(User.class, false);
        grid.addColumn(User::getId).setHeader("Id").setSortable(true);
        grid.addColumn(User::getUserName).setHeader("Name").setSortable(true);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.setDataProvider(getProvider(userRepository, searchField));
        grid.setHeightFull();

        add(searchField, grid);
    }
    private ConfigurableFilterDataProvider<User, Void, String> getProvider(UserRepository questionRepository, TextField searchField) {
        CallbackDataProvider<User, String> dataProvider = new CallbackDataProvider<>(
                query -> {
                    // Используем Stream для получения нужного диапазона данных из репозитория
                    // В реальном приложении здесь обычно используется JpaSpecificationExecutor с пагинацией
                    return questionRepository.findAllByRole(role)
                            .stream()
                            .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                            .limit(query.getLimit()); // Берем только нужное количество
                },
                // Метод count (подсчет общего количества результатов фильтрации)
                query -> questionRepository.countByRole(role)
        );

        return dataProvider.withConfigurableFilter();
    }
}
