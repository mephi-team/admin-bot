package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.mailings.dataproviders.TemplateDataProvider;
import team.mephi.adminbot.vaadin.mailings.presenter.TemplatePresenter;

import java.util.List;
import java.util.Set;

public class TemplateView extends VerticalLayout {
    private List<Long> selectedIds;

    public TemplateView(TemplatePresenter actions) {
        TemplateDataProvider provider = (TemplateDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_template_actions_label"),
                new Button(getTranslation("grid_template_actions_delete_label"), VaadinIcon.TRASH.create(), e -> {
                    if (!selectedIds.isEmpty()) {
                        actions.onDelete(selectedIds);
                    }
                })
        );

        setSizeFull();
        setPadding(false);

        Grid<SimpleTemplate> grid = new Grid<>(SimpleTemplate.class, false);
        grid.addColumn(SimpleTemplate::getName).setHeader(getTranslation("grid_template_header_name_label")).setSortable(true).setKey("name");
        grid.addColumn(SimpleTemplate::getText).setHeader(getTranslation("grid_template_header_text_label")).setSortable(true).setKey("bodyText");

        grid.addComponentColumn(item -> {
            Div group = new Div();
            group.addClassNames(LumoUtility.TextAlignment.RIGHT);
            Button editButton = new Button(new Icon(VaadinIcon.EDIT), e -> actions.onEdit(item));
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), e -> actions.onDelete(List.of(item.getId())));
            group.add(editButton, deleteButton);
            return group;
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("120px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
//        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addSelectionListener(selection -> {
            selectedIds = selection.getAllSelectedItems().stream().map(SimpleTemplate::getId).toList();
            gsa.setCount(selectedIds.size());
        });
        provider.getFilterableProvider().addDataProviderListener(e -> {
            grid.deselectAll();
        });

        var searchField = new SearchField(getTranslation("grid_template_search_placeholder"));
        searchField.addValueChangeListener(e -> {
            provider.getFilterableProvider().setFilter(e.getValue());
        });

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), gsa, grid);
    }
}
