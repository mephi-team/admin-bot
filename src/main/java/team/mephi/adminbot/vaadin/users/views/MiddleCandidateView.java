package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.QueryParameters;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.MiddleCandidateDataProvider;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MiddleCandidateView extends VerticalLayout {
    private List<Long> selectedIds;

    public MiddleCandidateView(MiddleCandidateDataProvider provider, UserActions actions) {
        var gsa = new GridSelectActions(
                new Button("Утвердить кандидатов", VaadinIcon.CHECK.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onAccept(selectedIds);
                }),
                new Button("Отклонить кандидатов", VaadinIcon.CLOSE.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onReject(selectedIds);
                }),
                new Button("Заблокировать пользователей", VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds);
                })
        );

        setSizeFull();
        setPadding(false);

        var grid = new Grid<>(UserDto.class, false);
        grid.addColumn(UserDto::getFullName).setHeader("Фамилия Имя").setSortable(true).setKey("name");
        grid.addColumn(UserDto::getEmail).setHeader("Email").setSortable(true).setKey("email");
        grid.addColumn(UserDto::getTgName).setHeader("Telegram").setSortable(true).setKey("telegram");
        grid.addColumn(UserDto::getPhoneNumber).setHeader("Телефон").setSortable(true).setKey("phone");
        grid.addColumn(UserDto::getPdConsent).setHeader("Согласия ПД").setSortable(true).setKey("pd");
        grid.addColumn(UserDto::getCohort).setHeader("Набор").setSortable(true).setKey("cohort");
        grid.addColumn(UserDto::getDirection).setHeader("Направление").setSortable(true).setKey("direction");
        grid.addColumn(UserDto::getCity).setHeader("Город").setSortable(true).setKey("city");
        grid.addColumn(createStatusComponentRenderer()).setHeader("Статус").setSortable(true).setKey("status");

        grid.addComponentColumn(item -> {
            Button confirmButton = new Button(new Icon(VaadinIcon.CHECK), e -> actions.onAccept(List.of(item.getId())));
            Button rejectButton = new Button(new Icon(VaadinIcon.CLOSE), e -> actions.onReject(List.of(item.getId())));
            Button viewButton = new Button(new Icon(VaadinIcon.EYE), e -> actions.onView(item.getId()));
            Button chatButton = new Button(new Icon(VaadinIcon.CHAT), e -> UI.getCurrent().navigate(Dialogs.class, new QueryParameters(Map.of("userId", List.of("" + item.getId())))));
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL), e -> actions.onEdit(item.getId()));
            Button deleteButton = new Button(new Icon(VaadinIcon.BAN), e -> actions.onDelete(List.of(item.getId())));
            if (item.getDelete()) {
                deleteButton.getElement().getStyle().set("color", "red");
            } else {
                deleteButton.getElement().getStyle().set("color", "black");
            }
            return new Span(rejectButton, confirmButton, viewButton, chatButton, editButton, deleteButton);
        }).setHeader("Действия").setWidth("290px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setSizeFull();
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(UserDto::getId).toList();
            gsa.setCount(selectedIds.size());
        });

        var searchField = new SearchField("Найти миддл-кандидита");
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), gsa, grid);
    }

    private static ComponentRenderer<Span, UserDto> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }
    private static final SerializableBiConsumer<Span, UserDto> statusComponentUpdater = (
            span, person) -> {
        String theme = switch (person.getStatus()) {
            case "ACTIVE" -> String.format("badge %s", "success");
            default -> String.format("badge %s", "error");
        };
        span.getElement().setAttribute("theme", theme);
        span.setText(person.getStatus());
    };
}