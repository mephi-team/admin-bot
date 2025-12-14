package team.mephi.adminbot.vaadin.views.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.components.SearchField;
import team.mephi.adminbot.vaadin.providers.UserProvider;

public class GuestsView extends VerticalLayout {
    private final String role;

    public GuestsView(UserRepository userRepository, String role) {
        this.role = role;

        setHeightFull();
        setPadding(false);

        final TextField searchField = new SearchField("Найти гостя");

        Grid<UserDto> grid = new Grid<>(UserDto.class, false);
        grid.addColumn(UserDto::getUserName).setHeader("Имя пользователя в Telegram").setSortable(true);
        grid.addColumn(UserDto::getTgName).setHeader("Telegram").setSortable(true);
        grid.addColumn(UserDto::getPdConsent).setHeader("Согласия ПД").setSortable(true);

        grid.addComponentColumn(item -> {
            Span group = new Span();
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> {
                System.out.println(item);
            });
            Button deleteButton = new Button(new Icon(VaadinIcon.FILE_REMOVE));
            editButton.addClickListener(e -> {
                System.out.println(item);
            });
            group.add(editButton, deleteButton);
            return group;
        }).setHeader("Действия").setWidth("120px").setFlexGrow(0);

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.setDataProvider(getProvider(userRepository, searchField));
        grid.setHeightFull();

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selection -> {
            // System.out.printf("Number of selected people: %s%n",
            // selection.getAllSelectedItems().size());
        });

        add(searchField, grid);
    }

    private ConfigurableFilterDataProvider<UserDto, Void, String> getProvider(UserRepository userRepository, TextField searchField) {
        UserProvider dataProvider = new UserProvider(userRepository, role);
        return dataProvider.withConfigurableFilter();
    }
}
