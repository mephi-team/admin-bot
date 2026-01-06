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
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.users.dataproviders.CandidateDataProvider;
import team.mephi.adminbot.vaadin.users.presenter.UsersPresenter;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CandidateView extends VerticalLayout {
    private List<Long> selectedIds;

    public CandidateView(UsersPresenter actions) {
        CandidateDataProvider provider = (CandidateDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new Button(getTranslation("grid_candidate_actions_accept_label"), VaadinIcon.CHECK.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onAccept(selectedIds);
                }),
                new Button(getTranslation("grid_candidate_actions_reject_label"), VaadinIcon.CLOSE.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onReject(selectedIds);
                }),
                new Button(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds);
                })
        );

        setSizeFull();
        setPadding(false);

        var grid = new Grid<>(SimpleUser.class, false);
        grid.addColumn(SimpleUser::getFullName).setHeader(getTranslation("grid_candidate_header_name_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("lastName");
        grid.addColumn(SimpleUser::getEmail).setHeader(getTranslation("grid_candidate_header_email_label")).setSortable(true).setResizable(true).setKey("email");
        grid.addColumn(SimpleUser::getTgId).setHeader(getTranslation("grid_candidate_header_telegram_label")).setSortable(true).setResizable(true).setKey("tgId");
        grid.addColumn(SimpleUser::getPhoneNumber).setHeader(getTranslation("grid_candidate_header_phone_label")).setSortable(true).setResizable(true).setKey("phoneNumber");
        grid.addColumn(SimpleUser::getPdConsent).setHeader(getTranslation("grid_candidate_header_pd_consent_label")).setSortable(true).setResizable(true).setKey("pdConsent");
        grid.addColumn(SimpleUser::getCohort).setHeader(getTranslation("grid_candidate_header_cohort_label")).setSortable(true).setResizable(true).setKey("cohort");
        grid.addColumn(u -> Objects.nonNull(u.getDirection()) ? u.getDirection().getName() : "").setHeader(getTranslation("grid_candidate_header_direction_label")).setSortable(true).setResizable(true).setKey("direction");
        grid.addColumn(SimpleUser::getCity).setHeader(getTranslation("grid_candidate_header_city_label")).setSortable(true).setResizable(true).setKey("city");

        grid.addComponentColumn(item -> {
            Button confirmButton = new Button(new Icon(VaadinIcon.CHECK), e -> actions.onAccept(List.of(item.getId())));
            Button rejectButton = new Button(new Icon(VaadinIcon.CLOSE), e -> actions.onReject(List.of(item.getId())));
            Button viewButton = new Button(new Icon(VaadinIcon.EYE), e -> actions.onView(item));
            Button chatButton = new Button(new Icon(VaadinIcon.CHAT), e -> UI.getCurrent().navigate(Dialogs.class, QueryParameters.of("userId", item.getId().toString())));
            Button editButton = new Button(new Icon(VaadinIcon.PENCIL), e -> actions.onEdit(item));
            Button blockButton = new Button(new Icon(VaadinIcon.BAN), e -> actions.onBlock(item));
            if (UserStatus.BLOCKED.name().equals(item.getStatus())) {
                blockButton.addClassNames(LumoUtility.TextColor.ERROR);
            } else {
                blockButton.addClassNames(LumoUtility.TextColor.BODY);
            }
            return new Span(rejectButton, confirmButton, viewButton, chatButton, editButton, blockButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("290px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        GridMultiSelectionModel<SimpleUser> selectionModel = (GridMultiSelectionModel<SimpleUser>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);
        grid.setSizeFull();
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(SimpleUser::getId).toList();
            gsa.setCount(selectedIds.size());
        });

        var searchField = new SearchField(getTranslation("grid_candidate_search_placeholder"));
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of(), Set.of("actions"));
        settingsPopover.setTarget(settingsBtn);

        var downloadBtn = new Button(VaadinIcon.DOWNLOAD_ALT.create());

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);
    }
}