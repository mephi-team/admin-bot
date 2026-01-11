package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.mailings.dataproviders.SentDataProvider;
import team.mephi.adminbot.vaadin.mailings.presenter.MailingsPresenter;
import team.mephi.adminbot.vaadin.service.DialogType;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class SentView extends VerticalLayout {
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
    private List<Long> selectedIds;

    public SentView(MailingsPresenter actions) {
        SentDataProvider provider = (SentDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_mailing_actions_label"),
                new SecondaryButton(getTranslation("grid_mailing_actions_delete_label"), VaadinIcon.TRASH.create(), e -> {
                    if (!selectedIds.isEmpty()) {
                        actions.onDelete(selectedIds, selectedIds.size() > 1 ? DialogType.DELETE_MAILING_ALL : DialogType.DELETE_MAILING, String.valueOf(selectedIds.size()));
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
        grid.addColumn(createStatusComponentRenderer()).setHeader(getTranslation("grid_mailing_header_status_label")).setSortable(true).setResizable(true).setWidth("110px").setKey("status");

        grid.addComponentColumn(item -> {
            Button retryButton = new IconButton(VaadinIcon.ROTATE_RIGHT.create(), e -> actions.onRetry(item, DialogType.RETRY_MAILING));
            retryButton.setVisible(item.getStatus().equals("PAUSED") || item.getStatus().equals("ERROR"));
            Button cancelButton = new IconButton(VaadinIcon.CLOSE_CIRCLE_O.create(), e -> actions.onCancel(item, DialogType.CANCEL_MAILING));
            cancelButton.setVisible(item.getStatus().equals("ACTIVE"));
            Button deleteButton = new IconButton(VaadinIcon.TRASH.create(), e -> actions.onDelete(List.of(item.getId()), DialogType.DELETE_MAILING));
            return new ButtonGroup(retryButton, cancelButton, deleteButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("120px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        GridMultiSelectionModel<?> selectionModel = (GridMultiSelectionModel<?>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);
//        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addSelectionListener(selection -> {
            selectedIds = selection.getAllSelectedItems().stream().map(SimpleMailing::getId).toList();
            gsa.setCount(selectedIds.size());
        });
        grid.setEmptyStateText(getTranslation("grid_mailing_empty_label"));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeName("neo");

        provider.getFilterableProvider().addDataProviderListener(e -> {
            grid.deselectAll();
        });

        var searchField = new SearchField(getTranslation("grid_mailing_search_placeholder"));
        searchField.addValueChangeListener(e -> {
            provider.getFilterableProvider().setFilter(e.getValue());
        });

        var settingsBtn = new IconButton(VaadinIcon.COG.create());
        var settingsPopover = new GridSettingsPopover(grid, Set.of(), Set.of("actions"));
        settingsPopover.setTarget(settingsBtn);

        var downloadBtn = new IconButton(VaadinIcon.DOWNLOAD_ALT.create(), e -> {
        });

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);
    }

    private static ComponentRenderer<Span, SimpleMailing> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }
}
