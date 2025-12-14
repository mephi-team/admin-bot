package team.mephi.adminbot.vaadin.views.users;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.components.SearchField;
import team.mephi.adminbot.vaadin.providers.UserProvider;

public class MiddleCandidateView extends VerticalLayout {
    private final String role;

    public MiddleCandidateView(UserRepository userRepository, String role) {
        this.role = role;

        setHeightFull();
        setPadding(false);

        final TextField searchField = new SearchField("Найти миддл-кандидита");

        Grid<UserDto> grid = new Grid<>(UserDto.class, false);
        grid.addColumn(UserDto::getFullName).setHeader("Фамилия Имя").setSortable(true);
        grid.addColumn(UserDto::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(UserDto::getTgName).setHeader("Telegram").setSortable(true);
        grid.addColumn(UserDto::getPhoneNumber).setHeader("Телефон").setSortable(true);
        grid.addColumn(UserDto::getPdConsent).setHeader("Согласия ПД").setSortable(true);
        grid.addColumn(UserDto::getCohort).setHeader("Набор").setSortable(true);
        grid.addColumn(UserDto::getDirection).setHeader("Направление").setSortable(true);
        grid.addColumn(UserDto::getCity).setHeader("Город").setSortable(true);
        grid.addColumn(UserDto::getStatus).setHeader("Статус").setSortable(true);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.setDataProvider(getProvider(userRepository, searchField));
        grid.setHeightFull();

        add(searchField, grid);
    }

    private ConfigurableFilterDataProvider<UserDto, Void, String> getProvider(UserRepository userRepository, TextField searchField) {
        UserProvider dataProvider = new UserProvider(userRepository, role);
        return dataProvider.withConfigurableFilter();
    }
}
