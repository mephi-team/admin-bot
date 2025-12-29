package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import team.mephi.adminbot.dto.MailingList;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.DraftDataProvider;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Set;

public class DraftView extends VerticalLayout {
    private List<Long> selectedIds;

    private static final SerializableBiConsumer<Span, MailingList> statusComponentUpdater = (
            span, person) -> {
        String theme = switch (person.getStatus()) {
            case "ACTIVE" -> String.format("badge %s", "success");
            default -> String.format("badge %s", "error");
        };
        span.getElement().setAttribute("theme", theme);
        span.setText(person.getStatus());
    };

    public DraftView(DraftDataProvider provider, CRUDActions actions) {
        var gsa = new GridSelectActions(getTranslation("grid_mailing_actions_label"),
                new Button(getTranslation("grid_mailing_actions_delete_label"), VaadinIcon.TRASH.create(), e -> {
                    if (!selectedIds.isEmpty()) {
                        actions.onDelete(selectedIds);
                    }
                })
        );

        setSizeFull();
        setPadding(false);

        LocalDateTimeRenderer<MailingList> dateRenderer = new LocalDateTimeRenderer<>(
                d -> d.getDate().atZone(ZoneOffset.of("+03:00")).toLocalDateTime(),
                () -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT));

        Grid<MailingList> grid = new Grid<>(MailingList.class, false);
        grid.addColumn(dateRenderer).setHeader(getTranslation("grid_mailing_header_date_label")).setSortable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("created_at");
        grid.addColumn(MailingList::getUsers).setHeader(getTranslation("grid_mailing_header_users_label")).setSortable(true).setKey("filters->>'users'");
        grid.addColumn(MailingList::getCohort).setHeader(getTranslation("grid_mailing_header_cohort_label")).setSortable(true).setKey("filters->>'cohort'");
        grid.addColumn(MailingList::getDirection).setHeader(getTranslation("grid_mailing_header_direction_label")).setSortable(true).setKey("filters->>'direction'");
        grid.addColumn(MailingList::getCurator).setHeader(getTranslation("grid_mailing_header_curator_label")).setSortable(true).setKey("filters->>'curator'");
        grid.addColumn(MailingList::getCity).setHeader(getTranslation("grid_mailing_header_city_label")).setSortable(true).setKey("filters->>'city'");
        grid.addColumn(MailingList::getText).setHeader(getTranslation("grid_mailing_header_text_label")).setSortable(true).setKey("description");
        grid.addColumn(createStatusComponentRenderer()).setHeader(getTranslation("grid_mailing_header_status_label")).setSortable(true).setKey("status");

        grid.addComponentColumn(item -> {
            Div group = new Div();
            group.getElement().getStyle().set("text-align","end");
            Button editButton = new Button(new Icon(VaadinIcon.EDIT), e -> actions.onEdit(item.getId()));
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), e -> actions.onDelete(List.of(item.getId())));
            group.add(editButton, deleteButton);
            return group;
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("120px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        GridMultiSelectionModel<?> selectionModel = (GridMultiSelectionModel<?>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);
//        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addSelectionListener(selection -> {
            selectedIds = selection.getAllSelectedItems().stream().map(MailingList::getId).toList();
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
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), gsa, grid);
    }

    private static ComponentRenderer<Span, MailingList> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }
}
