package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import team.mephi.adminbot.dto.TemplateListDto;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.TemplateDataProvider;

import java.util.List;
import java.util.Set;

public class TemplateView extends VerticalLayout {
    private List<Long> selectedIds;

    public TemplateView(TemplateDataProvider provider, CRUDActions actions) {
        var gsa = new GridSelectActions("Выбрано шаблонов: ",
                new Button("Удалить шаблоны", VaadinIcon.TRASH.create(), e -> {
                    if (!selectedIds.isEmpty()) {
                        actions.onDelete(selectedIds);
                    }
                })
        );

        setSizeFull();
        setPadding(false);

        Grid<TemplateListDto> grid = new Grid<>(TemplateListDto.class, false);
        grid.addColumn(TemplateListDto::getName).setHeader("Название").setSortable(true).setKey("name");
        grid.addColumn(TemplateListDto::getText).setHeader("Текст").setSortable(true).setKey("text");

        grid.addComponentColumn(item -> {
            Span group = new Span();
            Button editButton = new Button(new Icon(VaadinIcon.EDIT), e -> actions.onEdit(item.getId()));
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), e -> actions.onDelete(List.of(item.getId())));
            group.add(editButton, deleteButton);
            return group;
        }).setHeader("Действия").setWidth("120px").setFlexGrow(0).setKey("action");

        grid.setDataProvider(provider.getDataProvider());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addSelectionListener(selection -> {
            selectedIds = selection.getAllSelectedItems().stream().map(TemplateListDto::getId).toList();
            gsa.setCount(selectedIds.size());
        });
        provider.getFilterableProvider().addDataProviderListener(e -> {
            grid.deselectAll();
        });

        var searchField = new SearchField("Найти шаблон");
        searchField.addValueChangeListener(e -> {
            provider.getFilterableProvider().setFilter(e.getValue());
        });

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), gsa, grid);
    }
}
