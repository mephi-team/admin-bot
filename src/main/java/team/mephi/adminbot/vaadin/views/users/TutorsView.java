package team.mephi.adminbot.vaadin.views.users;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.QueryParameters;
import org.springframework.data.repository.CrudRepository;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.dto.TutorWithCounts;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.providers.ProviderGet;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class TutorsView extends VerticalLayout implements ProviderGet {
    private final TutorRepository tutorRepository;
    private final Grid<TutorWithCounts> grid;
    private final GridSelectActions actions;
    private List<Long> selectedIds;

    public TutorsView(TutorRepository tutorRepository, BiConsumer<Long, ProviderGet> onView, BiConsumer<Long, ProviderGet> onEdit, BiConsumer<List<Long>, ProviderGet> onDelete) {
        this.tutorRepository = tutorRepository;
        setHeightFull();
        setPadding(false);

        final TextField searchField = new SearchField("Найти куратора");

        grid = new Grid<>(TutorWithCounts.class, false);

        Button block = new Button("Заблокировать пользователей", new Icon(VaadinIcon.BAN), e -> {
            onDelete.accept(selectedIds, this);
        });
        actions = new GridSelectActions(block);

        grid.addColumn(TutorWithCounts::getFullName).setHeader("Фамилия Имя").setSortable(true).setKey("name");
        grid.addColumn(TutorWithCounts::getEmail).setHeader("Email").setSortable(true).setKey("email");
        grid.addColumn(TutorWithCounts::getTgId).setHeader("Telegram").setSortable(true).setKey("telegram");
        grid.addColumn(TutorWithCounts::getDirections).setHeader("Направление").setSortable(true).setKey("direction");
        grid.addColumn(TutorWithCounts::getStudentCount).setHeader("Кураторство").setSortable(true).setKey("curatorship");

        grid.addComponentColumn(item -> {
            Button dropButton = new Button("Кураторство", e -> {
                System.out.println(item);
            });
            Button viewButton = new Button(new Icon(VaadinIcon.EYE), e -> {
                onView.accept(item.getId(), this);
            });
            Button chatButton = new Button(new Icon(VaadinIcon.CHAT), e -> {
                UI.getCurrent().navigate(Dialogs.class, new QueryParameters(Map.of("userId", List.of("" + item.getId()))));
            });
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL), e -> {
                onEdit.accept(item.getId(), this);
            });
            Button deleteButton = new Button(new Icon(VaadinIcon.BAN), e -> {
                onDelete.accept(List.of(item.getId()), this);
            });
            if (item.getDelete()) {
                deleteButton.getElement().getStyle().set("color", "red");
            } else {
                deleteButton.getElement().getStyle().set("color", "black");
            }
            return new Span(dropButton, viewButton, chatButton, editButton, deleteButton);
        }).setHeader("Действия").setWidth("330px").setFlexGrow(0).setKey("actions");

        var filterableProvider = getProvider(tutorRepository, searchField);

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.setDataProvider(filterableProvider);
        grid.setHeightFull();

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selection -> {
            actions.setCount(selection.getAllSelectedItems().size());
            selectedIds = selection.getAllSelectedItems().stream().map(TutorWithCounts::getId).toList();
        });

        searchField.addValueChangeListener(e -> {
            filterableProvider.setFilter(e.getValue());
        });

        GridSettingsButton settings = new GridSettingsButton();
        GridSettingsPopover popover = new GridSettingsPopover(grid, Set.of());
        popover.setTarget(settings);
        SearchFragment headerLayout = new SearchFragment(searchField, settings);

        add(headerLayout, actions, grid);
    }

    private static ConfigurableFilterDataProvider<TutorWithCounts, Void, String> getProvider(TutorRepository tutorRepository, TextField searchField) {
        CallbackDataProvider<TutorWithCounts, String> dataProvider = new CallbackDataProvider<>(
                query -> {
                    return tutorRepository.findAllWithDirectionsAndStudents(searchField.getValue())
                            .stream()
                            .skip(query.getOffset())
                            .limit(query.getLimit());
                },
                query -> tutorRepository.countByName(searchField.getValue()),
                TutorWithCounts::getId
        );

        return dataProvider.withConfigurableFilter();
    }

    @Override
    public DataProvider<TutorWithCounts, ?> getProvider() {
        return grid.getDataProvider();
    }

    @Override
    public CrudRepository<?, Long> getRepository() {
        return tutorRepository;
    }

    @Override
    public Optional<SimpleUser> findSimpleUserById(Long id) {
        return tutorRepository.findSimpleUserById(id);
    }

    @Override
    public SimpleUser save(SimpleUser user) {
        Tutor fullUser = tutorRepository.findById(user.getId()).orElseThrow();
        fullUser.setFirstName(user.getFirstName());
        fullUser.setLastName(user.getLastName());
        fullUser = tutorRepository.save(fullUser);
        return new SimpleUser(fullUser.getId(), fullUser.getFirstName(), fullUser.getLastName(), fullUser.getEmail(), fullUser.getTgId());
    }

    @Override
    public void deleteById(Long id) {
        this.tutorRepository.deleteById(id);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        this.tutorRepository.deleteAllById(ids);
    }

    @Override
    public void refreshAll() {
        grid.getDataProvider().refreshAll();
    }
}
