package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import team.mephi.adminbot.dto.TutorWithCounts;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.users.actions.TutorActions;
import team.mephi.adminbot.vaadin.users.dataproviders.TutorDataProvider;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TutorView extends VerticalLayout {
    private List<Long> selectedIds;

    public TutorView(TutorDataProvider provider, TutorActions actions) {
        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new Button(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds);
                })
        );

        setSizeFull();
        setPadding(false);

        var grid = new Grid<>(TutorWithCounts.class, false);
        grid.addColumn(TutorWithCounts::getFullName).setHeader(getTranslation("grid_tutor_header_name_label")).setSortable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("last_name");
        grid.addColumn(TutorWithCounts::getEmail).setHeader(getTranslation("grid_tutor_header_email_label")).setSortable(true).setKey("email");
        grid.addColumn(TutorWithCounts::getTgId).setHeader(getTranslation("grid_tutor_header_telegram_label")).setSortable(true).setKey("tg_name");
        grid.addColumn(TutorWithCounts::getDirections).setHeader(getTranslation("grid_tutor_header_direction_label")).setKey("direction");
        grid.addColumn(TutorWithCounts::getStudentCount).setHeader(getTranslation("grid_tutor_header_curatorship_label")).setKey("curatorship");

        grid.addComponentColumn(item -> {
            Button dropButton = new Button(getTranslation("grid_tutor_action_curatorship_label"), e -> actions.onTutoring(item.getId()));
            Button viewButton = new Button(new Icon(VaadinIcon.EYE), e -> actions.onView(item.getId()));
            Button chatButton = new Button(new Icon(VaadinIcon.CHAT), e -> {
                UI.getCurrent().navigate(Dialogs.class, new QueryParameters(Map.of("userId", List.of("" + item.getId()))));
            });
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL), e -> actions.onEdit(item.getId()));
            Button blockButton = new Button(new Icon(VaadinIcon.BAN), e -> actions.onBlock(List.of(item.getId())));
            if (item.getDelete()) {
                blockButton.getElement().getStyle().set("color", "red");
            } else {
                blockButton.getElement().getStyle().set("color", "black");
            }
            return new Span(dropButton, viewButton, chatButton, editButton, blockButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("330px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        GridMultiSelectionModel<TutorWithCounts> selectionModel = (GridMultiSelectionModel<TutorWithCounts>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);
        grid.setSizeFull();
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(TutorWithCounts::getId).toList();
            gsa.setCount(selectedIds.size());
        });

        var searchField = new SearchField(getTranslation("grid_tutor_search_placeholder"));
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), gsa, grid);
    }
}