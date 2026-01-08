package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.users.dataproviders.MiddleCandidateDataProvider;
import team.mephi.adminbot.vaadin.users.presenter.UsersPresenter;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MiddleCandidateView extends VerticalLayout {
    private List<Long> selectedIds;

    public MiddleCandidateView(UsersPresenter actions) {
        MiddleCandidateDataProvider provider = (MiddleCandidateDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new Button(getTranslation("grid_middle_candidate_actions_accept_label"), VaadinIcon.CHECK.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onAccept(selectedIds, selectedIds.size() > 1 ? "accept_users_all" : "accept_users", "" + selectedIds.size());
                }),
                new Button(getTranslation("grid_middle_candidate_actions_reject_label"), VaadinIcon.CLOSE.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onReject(selectedIds, selectedIds.size() > 1 ? "reject_users_all" : "reject_users", "" + selectedIds.size());
                }),
                new Button(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds, "delete_users");
                })
        );

        setSizeFull();
        setPadding(false);

        var grid = new Grid<>(SimpleUser.class, false);
        grid.addColumn(SimpleUser::getFullName).setHeader(getTranslation("grid_middle_candidate_header_name_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("lastName");
        grid.addColumn(SimpleUser::getEmail).setHeader(getTranslation("grid_middle_candidate_header_email_label")).setSortable(true).setResizable(true).setKey("email");
        grid.addColumn(SimpleUser::getTgName).setHeader(getTranslation("grid_middle_candidate_header_telegram_label")).setSortable(true).setResizable(true).setKey("tgName");
        grid.addColumn(SimpleUser::getPhoneNumber).setHeader(getTranslation("grid_middle_candidate_header_phone_label")).setSortable(true).setResizable(true).setKey("phoneNumber");
        grid.addColumn(MyRenderers.createPdRenderer()).setHeader(getTranslation("grid_middle_candidate_header_pd_consent_label")).setSortable(true).setResizable(true).setKey("pdConsent");
        grid.addColumn(SimpleUser::getCohort).setHeader(getTranslation("grid_middle_candidate_header_cohort_label")).setSortable(true).setResizable(true).setKey("cohort");
        grid.addColumn(u -> Objects.nonNull(u.getDirection()) ? u.getDirection().getName() : "").setHeader(getTranslation("grid_middle_candidate_header_direction_label")).setSortable(true).setResizable(true).setKey("direction");
        grid.addColumn(SimpleUser::getCity).setHeader(getTranslation("grid_middle_candidate_header_city_label")).setSortable(true).setResizable(true).setKey("city");
        grid.addColumn(new ComponentRenderer<>(Span::new, statusComponentUpdater)).setHeader(getTranslation("grid_middle_candidate_header_status_label")).setSortable(true).setResizable(true).setKey("status");

        grid.addComponentColumn(item -> {
            Button confirmButton = new Button(VaadinIcon.CHECK.create(), e -> actions.onAccept(List.of(item.getId()), "accept_users"));
            Button rejectButton = new Button(VaadinIcon.CLOSE.create(), e -> actions.onReject(List.of(item.getId()), "reject_users"));
            Button viewButton = new Button(VaadinIcon.EYE.create(), e -> actions.onView(item, "users_view"));
            Button chatButton = new Button(VaadinIcon.CHAT.create(), e -> UI.getCurrent().navigate(Dialogs.class, QueryParameters.of("userId", item.getId().toString())));
            Button editButton = new Button(VaadinIcon.PENCIL.create(), e -> actions.onEdit(item, "users_edit"));
            Button blockButton = new Button(VaadinIcon.BAN.create(), e -> actions.onBlock(item, "users_blocked"));
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

        var searchField = new SearchField(getTranslation("grid_middle_candidate_search_placeholder"));
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of(), Set.of("actions"));
        settingsPopover.setTarget(settingsBtn);

        var downloadBtn = new Button(VaadinIcon.DOWNLOAD_ALT.create());

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);
    }

    private static final SerializableBiConsumer<Span, SimpleUser> statusComponentUpdater = (
            span, person) -> {
        String theme = switch (person.getStatus()) {
            case "ACTIVE" -> String.format("badge %s", "success");
            default -> String.format("badge %s", "error");
        };
        span.getElement().setAttribute("theme", theme);
        span.setText(person.getStatus());
    };
}