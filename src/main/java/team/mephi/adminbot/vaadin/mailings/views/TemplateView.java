package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.mailings.dataproviders.TemplateDataProvider;
import team.mephi.adminbot.vaadin.service.DialogType;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class TemplateView extends VerticalLayout {
    private List<Long> selectedIds;

    public TemplateView(CRUDPresenter<SimpleTemplate> actions) {
        TemplateDataProvider provider = (TemplateDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_template_actions_label"),
                new Button(getTranslation("grid_template_actions_delete_label"), VaadinIcon.TRASH.create(), e -> {
                    if (!selectedIds.isEmpty()) {
                        actions.onDelete(selectedIds, selectedIds.size() > 1 ? DialogType.DELETE_TEMPLATE_ALL : DialogType.DELETE_TEMPLATE, "" + selectedIds.size());
                    }
                })
        );

        setSizeFull();
        setPadding(false);

        LocalDateTimeRenderer<SimpleTemplate> dateRenderer = new LocalDateTimeRenderer<>(
                d -> d.getDate().atZone(ZoneOffset.of("+03:00")).toLocalDateTime(),
                () -> DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        Grid<SimpleTemplate> grid = new Grid<>(SimpleTemplate.class, false);
        grid.addColumn(SimpleTemplate::getName).setHeader(getTranslation("grid_template_header_name_label")).setWidth("300px").setFlexGrow(0).setSortable(true).setResizable(true).setKey("name");
        grid.addColumn(SimpleTemplate::getText).setHeader(getTranslation("grid_template_header_text_label")).setSortable(true).setResizable(true).setKey("bodyText");
        grid.addColumn(dateRenderer).setHeader(getTranslation("grid_template_header_date_label")).setSortable(true).setResizable(true).setKey("createdAt");

        grid.addComponentColumn(item -> {
            Div group = new Div();
            group.addClassNames(LumoUtility.TextAlignment.RIGHT);
            Button editButton = new Button(VaadinIcon.EDIT.create(), e -> actions.onEdit(item, DialogType.TEMPLATE_SAVED));
            Button deleteButton = new Button(VaadinIcon.TRASH.create(), e -> actions.onDelete(List.of(item.getId()), DialogType.DELETE_TEMPLATE));
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
        var settingsPopover = new GridSettingsPopover(grid, Set.of(), Set.of("actions"));
        settingsPopover.setTarget(settingsBtn);

        var downloadBtn = new Button(VaadinIcon.DOWNLOAD_ALT.create());

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);
    }
}
