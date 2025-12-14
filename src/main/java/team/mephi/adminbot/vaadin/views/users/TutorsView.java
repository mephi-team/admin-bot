package team.mephi.adminbot.vaadin.views.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import team.mephi.adminbot.dto.TutorWithCounts;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.vaadin.components.GridSettingsButton;
import team.mephi.adminbot.vaadin.components.GridSettingsPopover;
import team.mephi.adminbot.vaadin.components.SearchField;
import team.mephi.adminbot.vaadin.components.SearchFragment;

import java.util.Set;

public class TutorsView extends VerticalLayout {
    public TutorsView(TutorRepository tutorRepository) {
        setHeightFull();
        setPadding(false);

        final TextField searchField = new SearchField("Найти куратора");

        Grid<TutorWithCounts> grid = new Grid<>(TutorWithCounts.class, false);
        grid.addColumn(a -> a.getLastName() + " " + a.getFirstName())
                .setHeader("Фамилия Имя")
                .setSortable(true).setComparator(TutorWithCounts::getLastName).setKey("name");
        grid.addColumn(TutorWithCounts::getEmail).setHeader("Email").setSortable(true).setKey("email");
        grid.addColumn(TutorWithCounts::getTgId).setHeader("Telegram").setSortable(true).setKey("telegram");
        grid.addColumn(TutorWithCounts::getDirections).setHeader("Направление").setSortable(true).setKey("direction");
        grid.addColumn(TutorWithCounts::getStudentCount).setHeader("Кураторство").setSortable(true).setKey("curatorship");

        grid.addComponentColumn(item -> {
            Span group = new Span();
            Button dropButton = new Button("Кураторство");
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
                System.out.println(item);
            });
            Button deleteButton = new Button(new Icon(VaadinIcon.FILE_REMOVE));
            editButton.addClickListener(e -> {
                System.out.println(item);
            });
            group.add(dropButton, noteButton, chatButton, editButton, deleteButton);
            return group;
        }).setHeader("Действия").setWidth("330px").setFlexGrow(0).setKey("actions");

        var filterableProvider = getProvider(tutorRepository, searchField);

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.setDataProvider(filterableProvider);
        grid.setHeightFull();

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selection -> {
            // System.out.printf("Number of selected people: %s%n",
            // selection.getAllSelectedItems().size());
        });

        searchField.addValueChangeListener(e -> {
            filterableProvider.setFilter(e.getValue());
        });

        GridSettingsButton settings = new GridSettingsButton();
        GridSettingsPopover popover = new GridSettingsPopover(grid, Set.of());
        popover.setTarget(settings);
        SearchFragment headerLayout = new SearchFragment(searchField, settings);

        add(headerLayout, grid);
    }

    private static ConfigurableFilterDataProvider<TutorWithCounts, Void, String> getProvider(TutorRepository questionRepository, TextField searchField) {
        CallbackDataProvider<TutorWithCounts, String> dataProvider = new CallbackDataProvider<>(
                query -> {
                    // Используем Stream для получения нужного диапазона данных из репозитория
                    // В реальном приложении здесь обычно используется JpaSpecificationExecutor с пагинацией
                    return questionRepository.findAllWithDirectionsAndStudents(searchField.getValue())
                            .stream()
                            .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                            .limit(query.getLimit()); // Берем только нужное количество
                },
                // Метод count (подсчет общего количества результатов фильтрации)
                query -> questionRepository.countByName(searchField.getValue())
        );

        return dataProvider.withConfigurableFilter();
    }
}
