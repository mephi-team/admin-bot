package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.users.dataproviders.TutorDataProviderImpl;
import team.mephi.adminbot.vaadin.users.presenter.TutorPresenter;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.List;
import java.util.Set;

public class TutorView extends VerticalLayout {
    private List<Long> selectedIds;

    public TutorView(TutorPresenter actions) {
        TutorDataProviderImpl provider = (TutorDataProviderImpl) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new Button(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds, DialogType.DELETE_USERS);
                })
        );

        setSizeFull();
        setPadding(false);

        var grid = new Grid<>(SimpleTutor.class, false);
        grid.addColumn(SimpleTutor::getFullName).setHeader(getTranslation("grid_tutor_header_name_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("lastName");
        grid.addColumn(SimpleTutor::getCompetenceCenter).setHeader(getTranslation("grid_tutor_header_competence_center_label")).setSortable(true).setResizable(true).setKey("competenceCenter");
        grid.addColumn(SimpleTutor::getEmail).setHeader(getTranslation("grid_tutor_header_email_label")).setSortable(true).setResizable(true).setKey("email");
        grid.addColumn(SimpleTutor::getTgId).setHeader(getTranslation("grid_tutor_header_telegram_label")).setSortable(true).setResizable(true).setKey("tgId");
        grid.addColumn(MyRenderers.createTutorDirections()).setHeader(getTranslation("grid_tutor_header_direction_label")).setResizable(true).setKey("direction");
        grid.addColumn(MyRenderers.createCuratorshipRenderer()).setHeader(getTranslation("grid_tutor_header_curatorship_label")).setResizable(true).setKey("curatorship");

        grid.addComponentColumn(item -> {
            Button dropButton = new TextButton(getTranslation("grid_tutor_action_curatorship_label"), e -> actions.onTutoring(item, DialogType.TUTORS_UPDATED));
            Button viewButton = new IconButton(VaadinIcon.EYE.create(), e -> actions.onView(item, DialogType.TUTORS_VIEW));
            Button chatButton = new IconButton(VaadinIcon.CHAT.create(), e -> UI.getCurrent().navigate(Dialogs.class, QueryParameters.of("userId", item.getId().toString())));
            Button editButton = new IconButton(VaadinIcon.PENCIL.create(), e -> actions.onEdit(item, DialogType.TUTORS_EDIT));
            Button blockButton = new IconButton(VaadinIcon.BAN.create(), e -> actions.onBlock(item, DialogType.TUTORS_BLOCKED));
            if (UserStatus.BLOCKED.name().equals(item.getStatus())) {
                blockButton.addClassNames(LumoUtility.TextColor.ERROR);
            } else {
                blockButton.addClassNames(LumoUtility.TextColor.BODY);
            }
            return new Span(dropButton, viewButton, chatButton, editButton, blockButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("322px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        GridMultiSelectionModel<SimpleTutor> selectionModel = (GridMultiSelectionModel<SimpleTutor>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);
        grid.setSizeFull();
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(SimpleTutor::getId).toList();
            gsa.setCount(selectedIds.size());
        });
        grid.setEmptyStateText(getTranslation("grid_tutor_empty_label"));

        var searchField = new SearchField(getTranslation("grid_tutor_search_placeholder"));
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new IconButton(VaadinIcon.COG.create());
        var settingsPopover = new GridSettingsPopover(grid, Set.of(), Set.of("actions"));
        settingsPopover.setTarget(settingsBtn);

        var downloadBtn = new IconButton(VaadinIcon.DOWNLOAD_ALT.create(), e -> {});

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);
    }
}