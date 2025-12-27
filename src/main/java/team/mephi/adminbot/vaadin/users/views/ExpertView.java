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
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.ExpertDataProvider;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpertView extends VerticalLayout {
    private List<Long> selectedIds;

    public ExpertView(ExpertDataProvider provider, UserActions actions) {
        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new Button(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds);
                })
        );

        setSizeFull();
        setPadding(false);

        var grid = new Grid<>(UserDto.class, false);
        grid.addColumn(UserDto::getFullName).setHeader(getTranslation("grid_expert_header_name_label")).setSortable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("lastName");
        grid.addColumn(UserDto::getEmail).setHeader(getTranslation("grid_expert_header_email_label")).setSortable(true).setKey("email");
        grid.addColumn(UserDto::getTgName).setHeader(getTranslation("grid_expert_header_telegram_label")).setSortable(true).setKey("tgName");
        grid.addColumn(UserDto::getCohort).setHeader(getTranslation("grid_expert_header_cohort_label")).setSortable(true).setKey("cohort");
        grid.addColumn(UserDto::getDirection).setHeader(getTranslation("grid_expert_header_direction_label")).setSortable(true).setKey("direction");

        grid.addComponentColumn(item -> {
            Button dropButton = new Button(getTranslation("grid_expert_action_delete_label"), new Icon(VaadinIcon.CLOSE), e -> actions.onDelete(List.of(item.getId())));
            Button viewButton = new Button(new Icon(VaadinIcon.EYE), e -> actions.onView(item.getId()));
            Button chatButton = new Button(new Icon(VaadinIcon.CHAT), e -> UI.getCurrent().navigate(Dialogs.class, new QueryParameters(Map.of("userId", List.of("" + item.getId())))));
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL), e -> actions.onEdit(item.getId()));
            Button deleteButton = new Button(new Icon(VaadinIcon.BAN), e -> actions.onBlock(item.getId()));
            if (item.getDelete()) {
                deleteButton.getElement().getStyle().set("color", "red");
            } else {
                deleteButton.getElement().getStyle().set("color", "black");
            }
            return new Span(dropButton, viewButton, chatButton, editButton, deleteButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("320px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        GridMultiSelectionModel<UserDto> selectionModel = (GridMultiSelectionModel<UserDto>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);
        grid.setSizeFull();
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(UserDto::getId).toList();
            gsa.setCount(selectedIds.size());
        });

        var searchField = new SearchField(getTranslation("grid_expert_search_placeholder"));
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), gsa, grid);
    }
}