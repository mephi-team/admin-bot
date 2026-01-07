package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.mailings.dataproviders.DraftDataProvider;
import team.mephi.adminbot.vaadin.mailings.presenter.MailingsPresenter;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class DraftView extends VerticalLayout {
    private List<Long> selectedIds;

    private static final SerializableBiConsumer<Span, SimpleMailing> statusComponentUpdater = (
            span, person) -> {
        String theme = switch (person.getStatus()) {
            case "ACTIVE" -> "badge";
            case "DRAFT" -> String.format("badge %s", "contrast");
            case "PAUSED" -> String.format("badge %s", "warning");
            case "FINISHED" -> String.format("badge %s", "success");
            default -> String.format("badge %s", "error");
        };
        span.getElement().setAttribute("theme", theme);
        span.setText(span.getTranslation("mailing_status_" + person.getStatus().toLowerCase() + "_label"));
    };

    public DraftView(MailingsPresenter actions) {
        DraftDataProvider provider = (DraftDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_mailing_actions_label"),
                new Button(getTranslation("grid_mailing_actions_delete_label"), VaadinIcon.TRASH.create(), e -> {
                    if (!selectedIds.isEmpty()) {
                        actions.onDelete(selectedIds, selectedIds.size() > 1 ? "delete_mailing_all" : "delete_mailing", selectedIds.size());
                    }
                })
        );

        setSizeFull();
        setPadding(false);

        LocalDateTimeRenderer<SimpleMailing> dateRenderer = new LocalDateTimeRenderer<>(
                d -> d.getDate().atZone(ZoneOffset.of("+03:00")).toLocalDateTime(),
                () -> DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        Grid<SimpleMailing> grid = new Grid<>(SimpleMailing.class, false);
        grid.addColumn(dateRenderer).setHeader(getTranslation("grid_mailing_header_date_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("created_at");
        grid.addColumn(SimpleMailing::getUsers).setHeader(getTranslation("grid_mailing_header_users_label")).setSortable(true).setResizable(true).setKey("filters->>'users'");
        grid.addColumn(SimpleMailing::getCohort).setHeader(getTranslation("grid_mailing_header_cohort_label")).setSortable(true).setResizable(true).setKey("filters->>'cohort'");
        grid.addColumn(SimpleMailing::getDirection).setHeader(getTranslation("grid_mailing_header_direction_label")).setSortable(true).setResizable(true).setKey("filters->>'direction'");
        grid.addColumn(SimpleMailing::getCurator).setHeader(getTranslation("grid_mailing_header_curator_label")).setSortable(true).setResizable(true).setKey("filters->>'curator'");
        grid.addColumn(SimpleMailing::getCity).setHeader(getTranslation("grid_mailing_header_city_label")).setSortable(true).setResizable(true).setKey("filters->>'city'");
        grid.addColumn(SimpleMailing::getText).setHeader(getTranslation("grid_mailing_header_text_label")).setSortable(true).setResizable(true).setKey("description");
        grid.addColumn(createStatusComponentRenderer()).setHeader(getTranslation("grid_mailing_header_status_label")).setSortable(true).setResizable(true).setKey("status");

        grid.addComponentColumn(item -> {
            Div group = new Div();
            group.addClassNames(LumoUtility.TextAlignment.RIGHT);
            Button editButton = new Button(VaadinIcon.EDIT.create(), e -> actions.onEdit(item, "mailing_saved"));
            Button deleteButton = new Button(VaadinIcon.TRASH.create(), e -> actions.onDelete(List.of(item.getId()), "delete_mailing"));
            group.add(editButton, deleteButton);
            return group;
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("120px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        GridMultiSelectionModel<?> selectionModel = (GridMultiSelectionModel<?>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);
//        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addSelectionListener(selection -> {
            selectedIds = selection.getAllSelectedItems().stream().map(SimpleMailing::getId).toList();
            gsa.setCount(selectedIds.size());
        });
        provider.getFilterableProvider().addDataProviderListener(e -> {
            grid.deselectAll();
        });

        var searchField = new SearchField(getTranslation("grid_mailing_search_placeholder"));
        searchField.addValueChangeListener(e -> {
            provider.getFilterableProvider().setFilter(e.getValue());
        });

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of(), Set.of("actions"));
        settingsPopover.setTarget(settingsBtn);

        var downloadBtn = new Button(VaadinIcon.DOWNLOAD_ALT.create());

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);
    }

    private static ComponentRenderer<Span, SimpleMailing> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }
}
