package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.Route;
import team.mephi.adminbot.dto.MailingList;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.repository.MailingRepository;
import team.mephi.adminbot.vaadin.components.GridSettingsButton;
import team.mephi.adminbot.vaadin.components.GridSettingsPopover;
import team.mephi.adminbot.vaadin.components.SearchField;
import team.mephi.adminbot.vaadin.components.SearchFragment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;


@Route(value = "/mailings", layout = DialogsLayout.class)
public class Mailings extends VerticalLayout {
    public Mailings(MailingRepository mailingRepository) {
        setHeightFull();

        add(new H1("Рассылки"));

        Grid<MailingList> grid = new Grid<>(MailingList.class, false);
        grid.addColumn(MailingList::getDate).setHeader("Дата").setSortable(true).setKey("date");
        grid.addColumn(MailingList::getUsers).setHeader("Пользователи").setSortable(true).setKey("users");
        grid.addColumn(MailingList::getCohort).setHeader("Набор").setSortable(true).setKey("cohort");
        grid.addColumn(MailingList::getDirection).setHeader("Направление").setSortable(true).setKey("direction");
        grid.addColumn(MailingList::getCurator).setHeader("Куратор").setSortable(true).setKey("curator");
        grid.addColumn(MailingList::getCity).setHeader("Город").setSortable(true).setKey("city");
        grid.addColumn(MailingList::getText).setHeader("Текст сообщения").setSortable(true).setKey("text");
        grid.addColumn(createStatusComponentRenderer()).setHeader("Статус").setSortable(true).setKey("status");

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
        }).setHeader("Действия").setWidth("120px").setFlexGrow(0).setKey("action");

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);

        final TextField searchField = new SearchField("Найти рассылку");

        var filterableProvider = getProvider(mailingRepository, searchField);
        grid.setDataProvider(filterableProvider);

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selection -> {
            // System.out.printf("Number of selected people: %s%n",
            // selection.getAllSelectedItems().size());
        });

        searchField.addValueChangeListener(e -> {
            // setFilter will refresh the data provider and trigger data
            // provider fetch / count queries. As a side effect, the pagination
            // controls will be updated.
            filterableProvider.setFilter(e.getValue());
        });

        GridSettingsButton settings = new GridSettingsButton();
        GridSettingsPopover popover = new GridSettingsPopover(grid, Set.of());
        popover.setTarget(settings);
        SearchFragment headerLayout = new SearchFragment(searchField, settings);

        add(headerLayout, grid);
    }

    private static ConfigurableFilterDataProvider<MailingList, Void, String> getProvider(MailingRepository mailingRepository, TextField searchField) {
        CallbackDataProvider<MailingList, String> dataProvider = new CallbackDataProvider<>(
                query -> {
                    // Используем Stream для получения нужного диапазона данных из репозитория
                    // В реальном приложении здесь обычно используется JpaSpecificationExecutor с пагинацией
                    return mailingRepository.findMailingByName(searchField.getValue())
                            .stream()
                            .map(m -> MailingList.builder()
                                    .id(m.getId())
                                    .name(m.getName())
                                    .date(LocalDateTime.ofInstant(m.getCreatedAt(), ZoneId.of("UTC")))
                                    .users(m.getFilters() != null ? m.getFilters().getUsers() : "")
                                    .cohort(m.getFilters() != null ? m.getFilters().getCurator() : "")
                                    .direction(m.getFilters() != null ? m.getFilters().getDirection() : "")
                                    .curator(m.getFilters() != null ? m.getFilters().getCurator() : "")
                                    .city(m.getFilters() != null ? m.getFilters().getCity() : "")
                                    .status(m.getStatus().name())
                                    .build())
                            .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                            .limit(query.getLimit()); // Берем только нужное количество
                },
                // Метод count (подсчет общего количества результатов фильтрации)
                query -> mailingRepository.countByName(searchField.getValue())
        );

        return dataProvider.withConfigurableFilter();
    }

    private static final SerializableBiConsumer<Span, MailingList> statusComponentUpdater = (
            span, person) -> {
        String theme = switch (person.getStatus()) {
            case "ACTIVE" -> String.format("badge %s","success");
            default -> String.format("badge %s","error");
        };
        span.getElement().setAttribute("theme", theme);
        span.setText(person.getStatus());
    };

    private static ComponentRenderer<Span, MailingList> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }
}
