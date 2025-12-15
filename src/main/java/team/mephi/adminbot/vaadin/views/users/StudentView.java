package team.mephi.adminbot.vaadin.views.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.CrudRepository;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.providers.ProviderGet;
import team.mephi.adminbot.vaadin.providers.UserProvider;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class StudentView extends VerticalLayout implements ProviderGet {
    private final UserRepository userRepository;
    private final String role;
    private final Grid<UserDto> grid;
    private final GridSelectActions actions;
    private List<Long> selectedIds;

    public StudentView(UserRepository userRepository, String role, Consumer<Persistable<Long>> onEdit, Consumer<Persistable<Long>> onDelete) {
        this.role = role;
        this.userRepository = userRepository;

        setHeightFull();
        setPadding(false);

        final TextField searchField = new SearchField("Найти студента");

        grid = new Grid<>(UserDto.class, false);

        Button block = new Button("Заблокировать пользователей", new Icon(VaadinIcon.BAN), e -> {
            userRepository.deleteAllById(selectedIds);
            grid.getDataProvider().refreshAll();
        });
        actions = new GridSelectActions(block);

        grid.addColumn(UserDto::getFullName).setHeader("Фамилия Имя").setSortable(true).setKey("name");
        grid.addColumn(UserDto::getEmail).setHeader("Email").setSortable(true).setKey("email");
        grid.addColumn(UserDto::getTgName).setHeader("Telegram").setSortable(true).setKey("telegram");
        grid.addColumn(UserDto::getPhoneNumber).setHeader("Телефон").setSortable(true).setKey("phone");
        grid.addColumn(UserDto::getCohort).setHeader("Набор").setSortable(true).setKey("cohort");
        grid.addColumn(UserDto::getDirection).setHeader("Направление").setSortable(true).setKey("direction");
        grid.addColumn(UserDto::getCity).setHeader("Город").setSortable(true).setKey("city");
        grid.addColumn(UserDto::getCity).setHeader("Куратор").setSortable(true).setKey("curator");

        grid.addComponentColumn(item -> {
            Span group = new Span();
            Button dropButton = new Button("Отчислить", new Icon(VaadinIcon.CLOSE));
            dropButton.addClickListener(e -> {
                System.out.println(item);
            });
            Button noteButton = new Button(new Icon(VaadinIcon.NOTEBOOK));
            noteButton.addClickListener(e -> {
                System.out.println(item);
            });
            Button chatButton = new Button(new Icon(VaadinIcon.CHAT));
            chatButton.addClickListener(e -> {
                System.out.println(item);
            });
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> {
                onEdit.accept(item);
            });
            Button deleteButton = new Button(new Icon(VaadinIcon.BAN));
            if (item.getDelete()) {
                deleteButton.getElement().getStyle().set("color", "red");
            } else {
                deleteButton.getElement().getStyle().set("color", "black");
            }
            deleteButton.addClickListener(e -> {
                onDelete.accept(item);
            });
            group.add(dropButton, noteButton, chatButton, editButton, deleteButton);
            return group;
        }).setHeader("Действия").setWidth("340px").setFlexGrow(0).setKey("actions");

        var filterableProvider = getProvider(userRepository, searchField);

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.setDataProvider(filterableProvider);
        grid.setHeightFull();

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selection -> {
            actions.setCount(selection.getAllSelectedItems().size());
            selectedIds = selection.getAllSelectedItems().stream().map(UserDto::getId).toList();
        });

        searchField.addValueChangeListener(e -> {
            filterableProvider.setFilter(e.getValue());
        });

        GridSettingsButton settings = new GridSettingsButton();
        GridSettingsPopover popover = new GridSettingsPopover(grid, Set.of("email", "phone", "cohort"));
        popover.setTarget(settings);
        SearchFragment headerLayout = new SearchFragment(searchField, settings);

        add(headerLayout, actions, grid);
    }

    @Override
    public DataProvider<UserDto, ?> getProvider() {
        return grid.getDataProvider();
    }

    @Override
    public CrudRepository<?, Long> getRepository() {
        return userRepository;
    }

    private ConfigurableFilterDataProvider<UserDto, Void, String> getProvider(UserRepository userRepository, TextField searchField) {
        UserProvider dataProvider = new UserProvider(userRepository, role, searchField);
        return dataProvider.withConfigurableFilter();
    }
}
