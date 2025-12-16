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
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.providers.ProviderGet;
import team.mephi.adminbot.vaadin.providers.UserProvider;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class CandidateView extends VerticalLayout implements ProviderGet {
    private final UserRepository userRepository;
    private final String role;
    private final Grid<UserDto> grid;
    private final GridSelectActions actions;
    private List<Long> selectedIds;

    public CandidateView(UserRepository userRepository, String role, BiConsumer<Persistable<Long>, ProviderGet> onEdit, BiConsumer<Persistable<Long>, ProviderGet> onDelete) {
        this.role = role;
        this.userRepository = userRepository;

        setHeightFull();
        setPadding(false);

        final TextField searchField = new SearchField("Найти кандидита");

        grid = new Grid<>(UserDto.class, false);

        Button accept = new Button("Утвердить кандидатов", new Icon(VaadinIcon.CHECK));
        Button reject = new Button("Отклонить кандидатов", new Icon(VaadinIcon.CLOSE));
        Button block = new Button("Заблокировать пользователей", new Icon(VaadinIcon.BAN), e -> {
            userRepository.deleteAllById(selectedIds);
            grid.getDataProvider().refreshAll();
        });
        actions = new GridSelectActions(accept, reject, block);

        grid.addColumn(UserDto::getFullName).setHeader("Фамилия Имя").setSortable(true).setKey("name");
        grid.addColumn(UserDto::getEmail).setHeader("Email").setSortable(true).setKey("email");
        grid.addColumn(UserDto::getTgName).setHeader("Telegram").setSortable(true).setKey("telegram");
        grid.addColumn(UserDto::getPhoneNumber).setHeader("Телефон").setSortable(true).setKey("phone");
        grid.addColumn(UserDto::getPdConsent).setHeader("Согласия ПД").setSortable(true).setKey("pd");
        grid.addColumn(UserDto::getCohort).setHeader("Набор").setSortable(true).setKey("cohort");
        grid.addColumn(UserDto::getDirection).setHeader("Направление").setSortable(true).setKey("direction");
        grid.addColumn(UserDto::getCity).setHeader("Город").setSortable(true).setKey("city");

        grid.addComponentColumn(item -> {
            Button confirmButton = new Button(new Icon(VaadinIcon.CLOSE), e -> {
                System.out.println(item);
            });
            Button rejectButton = new Button(new Icon(VaadinIcon.CHECK), e -> {
                System.out.println(item);
            });
            Button noteButton = new Button(new Icon(VaadinIcon.NOTEBOOK), e -> {
                System.out.println(item);
            });
            Button chatButton = new Button(new Icon(VaadinIcon.CHAT), e -> {
                System.out.println(item);
            });
            Button editButton = new Button(new Icon(VaadinIcon.EDIT), e -> {
                onEdit.accept(item, this);
            });
            Button deleteButton = new Button(new Icon(VaadinIcon.BAN), e -> {
                onDelete.accept(item, this);
            });
            if (item.getDelete()) {
                deleteButton.getElement().getStyle().set("color", "red");
            } else {
                deleteButton.getElement().getStyle().set("color", "black");
            }
            return new Span(confirmButton, rejectButton, noteButton, chatButton, editButton, deleteButton);
        }).setHeader("Действия").setWidth("290px").setFlexGrow(0).setKey("actions");

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

    @Override
    public Optional<SimpleUser> findSimpleUserById(Long id) {
        return userRepository.findSimpleUserById(id);
    }

    @Override
    public SimpleUser save(SimpleUser user) {
        User fullUser = userRepository.findById(user.getId()).orElseThrow();
        fullUser.setFirstName(user.getFirstName());
        fullUser.setLastName(user.getLastName());
        fullUser = userRepository.save(fullUser);
        return new SimpleUser(fullUser.getId(), fullUser.getFirstName(), fullUser.getLastName(), fullUser.getEmail(), fullUser.getTgId());
    }

    @Override
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public void refreshAll() {
        grid.getDataProvider().refreshAll();
    }

    private ConfigurableFilterDataProvider<UserDto, Void, String> getProvider(UserRepository userRepository, TextField searchField) {
        UserProvider dataProvider = new UserProvider(userRepository, role, searchField);
        return dataProvider.withConfigurableFilter();
    }
}
