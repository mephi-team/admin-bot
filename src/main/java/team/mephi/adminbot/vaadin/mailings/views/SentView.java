package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import team.mephi.adminbot.dto.MailingList;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.SentDataProvider;

import java.util.List;
import java.util.Set;

public class SentView extends VerticalLayout {
    private List<Long> selectedIds;

    private static final SerializableBiConsumer<Span, MailingList> statusComponentUpdater = (
            span, person) -> {
        String theme = switch (person.getStatus()) {
            case "ACTIVE" -> String.format("badge %s", "success");
            default -> String.format("badge %s", "error");
        };
        span.getElement().setAttribute("theme", theme);
        span.setText(person.getStatus());
    };

    public SentView(SentDataProvider provider, MailingActions actions) {
        var gsa = new GridSelectActions("Выбрано рассылок: ",
                new Button("Удалить рассылки", VaadinIcon.TRASH.create(), e -> {
                    if (!selectedIds.isEmpty()) {
                        actions.onDelete(selectedIds);
                    }
                })
        );

        setSizeFull();

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
            Button editButton = new Button(new Icon(VaadinIcon.EDIT), e -> System.out.println(item));
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), e -> actions.onDelete(List.of(item.getId())));
            group.add(editButton, deleteButton);
            return group;
        }).setHeader("Действия").setWidth("120px").setFlexGrow(0).setKey("action");

        grid.setDataProvider(provider.getDataProvider());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addSelectionListener(selection -> {
            selectedIds = selection.getAllSelectedItems().stream().map(MailingList::getId).toList();
            gsa.setCount(selectedIds.size());
        });
        provider.getFilterableProvider().addDataProviderListener(e -> {
            grid.deselectAll();
        });

        var searchField = new SearchField("Найти рассылку");
        searchField.addValueChangeListener(e -> {
            provider.getFilterableProvider().setFilter(e.getValue());
        });

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), gsa, grid);
    }

    private static ComponentRenderer<Span, MailingList> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }
}
