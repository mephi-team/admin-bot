package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.vaadin.components.ButtonGroup;
import team.mephi.adminbot.vaadin.components.GridSelectActions;
import team.mephi.adminbot.vaadin.components.GridSettingsPopover;
import team.mephi.adminbot.vaadin.components.SearchFragment;
import team.mephi.adminbot.vaadin.components.buttons.IconButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.fields.SearchField;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.users.dataproviders.GuestsDataProvider;
import team.mephi.adminbot.vaadin.users.presenter.BlockingPresenter;

import java.util.List;
import java.util.Set;

public class GuestsView extends VerticalLayout {
    private List<Long> selectedIds;

    public GuestsView(BlockingPresenter actions) {
        GuestsDataProvider provider = (GuestsDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new SecondaryButton(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds, DialogType.DELETE_USERS);
                })
        );

        setSizeFull();
        setPadding(false);

        var grid = new Grid<>(SimpleUser.class, false);
        grid.addColumn(SimpleUser::getTgName).setHeader(getTranslation("grid_guests_header_name_label")).setSortable(true).setResizable(true).setKey("tgName");
        grid.addColumn(SimpleUser::getTgId).setHeader(getTranslation("grid_guests_header_telegram_label")).setSortable(true).setResizable(true).setKey("tgId");
        grid.addColumn(SimpleUser::getFullName).setHeader(getTranslation("grid_candidate_header_name_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("lastName");
        grid.addColumn(SimpleUser::getEmail).setHeader(getTranslation("grid_candidate_header_email_label")).setSortable(true).setResizable(true).setKey("email");
        grid.addColumn(SimpleUser::getPhoneNumber).setHeader(getTranslation("grid_candidate_header_phone_label")).setSortable(true).setResizable(true).setKey("phoneNumber");
        grid.addColumn(SimpleUser::getCity).setHeader(getTranslation("grid_candidate_header_city_label")).setSortable(true).setResizable(true).setKey("city");
        grid.addColumn(MyRenderers.createPdRenderer()).setHeader(getTranslation("grid_guests_header_pd_consent_label")).setSortable(true).setResizable(true).setKey("pdConsent");

        grid.addComponentColumn(item -> {
            Button viewButton = new IconButton(VaadinIcon.EYE.create(), e -> actions.onView(item, DialogType.USERS_VIEW));
            Button blockButton = new IconButton(VaadinIcon.BAN.create(), e -> actions.onBlock(item, DialogType.USERS_BLOCKED));
            if (UserStatus.BLOCKED.name().equals(item.getStatus())) {
                blockButton.addClassNames(LumoUtility.TextColor.ERROR);
            } else {
                blockButton.addClassNames(LumoUtility.TextColor.BODY);
            }
            return new ButtonGroup(viewButton, blockButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("112px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setSizeFull();
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(SimpleUser::getId).toList();
            gsa.setCount(selectedIds.size());
        });
        grid.setEmptyStateText(getTranslation("grid_guests_empty_label"));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeName("neo");

        var searchField = new SearchField(getTranslation("grid_guests_search_placeholder"));
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new IconButton(VaadinIcon.COG_O.create());
        var settingsPopover = new GridSettingsPopover(grid, Set.of("lastName", "email", "phoneNumber", "city"), Set.of("actions"));
        settingsPopover.setTarget(settingsBtn);

        var downloadBtn = new IconButton(VaadinIcon.DOWNLOAD_ALT.create(), e -> {
        });

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);
    }
}