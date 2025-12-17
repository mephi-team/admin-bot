package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.dataproviders.CandidateDataProvider;
import team.mephi.adminbot.vaadin.users.dataproviders.GuestsDataProvider;

import java.util.List;
import java.util.Set;

public class CandidateView extends VerticalLayout {

    private final Grid<UserDto> grid;
    private final GridSelectActions actions;
    private final CandidateDataProvider provider;
    private List<Long> selectedIds;

    public CandidateView(CandidateDataProvider provider, UserActions actions) {
        this.provider = provider;
        this.actions = new GridSelectActions(new Button("Заблокировать", VaadinIcon.BAN.create(), e -> {
            if (!selectedIds.isEmpty())
                actions.onDelete(selectedIds);
        }));

        setSizeFull();
        setPadding(false);

        grid = new Grid<>(UserDto.class, false);
        grid.setSizeFull();
        grid.addColumn(UserDto::getFullName).setHeader("Имя пользователя").setKey("name");
        grid.addColumn(UserDto::getTgName).setHeader("Telegram").setKey("telegram");
        grid.addColumn(UserDto::getPdConsent).setHeader("Согласия ПД").setKey("pd_consent");

        grid.addComponentColumn(item -> {
            var viewBtn = new Button(VaadinIcon.EYE.create(), e -> actions.onView(item.getId()));
            var blockBtn = new Button(VaadinIcon.BAN.create(), e -> actions.onDelete(List.of(item.getId())));
            return new Span(viewBtn, blockBtn);
        }).setHeader("Действия").setWidth("120px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getFilterableProvider());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(sel -> {
            selectedIds = sel.getAllSelectedItems().stream().map(UserDto::getId).toList();
            this.actions.setCount(selectedIds.size());
        });

        var searchField = new SearchField("Найти гостя");
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), this.actions, grid);
    }
}