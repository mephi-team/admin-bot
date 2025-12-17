package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import team.mephi.adminbot.dto.TutorWithCounts;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.TutorDataProvider;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TutorView extends VerticalLayout {

    private final Grid<TutorWithCounts> grid;
    private final GridSelectActions gsa;
    private final TutorDataProvider provider;
    private List<Long> selectedIds;

    public TutorView(TutorDataProvider provider, UserActions actions) {
        this.provider = provider;
        this.gsa = new GridSelectActions(
                new Button("Заблокировать пользователей", VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds);
                })
        );

        setSizeFull();
        setPadding(false);

        grid = new Grid<>(TutorWithCounts.class, false);
        grid.addColumn(TutorWithCounts::getFullName).setHeader("Фамилия Имя").setSortable(true).setKey("name");
        grid.addColumn(TutorWithCounts::getEmail).setHeader("Email").setSortable(true).setKey("email");
        grid.addColumn(TutorWithCounts::getTgId).setHeader("Telegram").setSortable(true).setKey("telegram");
        grid.addColumn(TutorWithCounts::getDirections).setHeader("Направление").setSortable(true).setKey("direction");
        grid.addColumn(TutorWithCounts::getStudentCount).setHeader("Кураторство").setSortable(true).setKey("curatorship");

        grid.addComponentColumn(item -> {
            Button dropButton = new Button("Кураторство", e -> System.out.println(item));
            Button viewButton = new Button(new Icon(VaadinIcon.EYE), e -> actions.onView(item.getId()));
            Button chatButton = new Button(new Icon(VaadinIcon.CHAT), e -> {
                UI.getCurrent().navigate(Dialogs.class, new QueryParameters(Map.of("userId", List.of("" + item.getId()))));
            });
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL), e -> actions.onEdit(item.getId()));
            Button deleteButton = new Button(new Icon(VaadinIcon.BAN), e -> actions.onDelete(List.of(item.getId())));
            if (item.getDelete()) {
                deleteButton.getElement().getStyle().set("color", "red");
            } else {
                deleteButton.getElement().getStyle().set("color", "black");
            }
            return new Span(dropButton, viewButton, chatButton, editButton, deleteButton);
        }).setHeader("Действия").setWidth("330px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getFilterableProvider());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setSizeFull();
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(TutorWithCounts::getId).toList();
            this.gsa.setCount(selectedIds.size());
        });

        var searchField = new SearchField("Найти куратора");
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), this.gsa, grid);
    }
}