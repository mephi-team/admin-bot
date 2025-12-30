package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.users.dataproviders.GuestsDataProvider;
import team.mephi.adminbot.vaadin.users.presenter.GuestPresenter;

import java.util.List;
import java.util.Set;

public class GuestsView extends VerticalLayout {
    private List<Long> selectedIds;

    public GuestsView(GuestPresenter actions) {
        GuestsDataProvider provider = (GuestsDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_users_actions_label"),
                new Button(getTranslation("grid_users_actions_block_label"), VaadinIcon.BAN.create(), e -> {
                    if (!selectedIds.isEmpty())
                        actions.onDelete(selectedIds);
                })
        );

        setSizeFull();
        setPadding(false);

        var grid = new Grid<>(UserDto.class, false);
        grid.addColumn(UserDto::getFullName).setHeader(getTranslation("grid_guests_header_name_label")).setSortable(true).setKey("userName");
        grid.addColumn(UserDto::getTgName).setHeader(getTranslation("grid_guests_header_telegram_label")).setSortable(true).setKey("tgName");
        grid.addColumn(UserDto::getPdConsent).setHeader(getTranslation("grid_guests_header_pd_consent_label")).setSortable(true).setKey("pdConsent");

        grid.addComponentColumn(item -> {
            Button viewButton = new Button(new Icon(VaadinIcon.EYE), e -> actions.onView(item.getId()));
            Button blockButton = new Button(new Icon(VaadinIcon.BAN), e -> actions.onBlock(item.getId()));
            if (item.getStatus().equals(UserStatus.BLOCKED.name())) {
                blockButton.addClassNames(LumoUtility.TextColor.ERROR);
            } else {
                blockButton.addClassNames(LumoUtility.TextColor.BODY);
            }
            return new Span(viewButton, blockButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("120px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setSizeFull();
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(UserDto::getId).toList();
            gsa.setCount(selectedIds.size());
        });

        var searchField = new SearchField(getTranslation("grid_guests_search_placeholder"));
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), gsa, grid);
    }
}