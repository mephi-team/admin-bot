package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.components.ButtonGroup;
import team.mephi.adminbot.vaadin.components.GridSelectActions;
import team.mephi.adminbot.vaadin.components.buttons.IconButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.grid.AbstractGridView;
import team.mephi.adminbot.vaadin.components.grid.GridViewConfig;
import team.mephi.adminbot.vaadin.mailings.dataproviders.TemplateDataProvider;
import team.mephi.adminbot.vaadin.service.DialogType;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class TemplateView extends AbstractGridView<SimpleTemplate> {

    private final CRUDPresenter<SimpleTemplate> actions;

    public TemplateView(CRUDPresenter<SimpleTemplate> actions) {
        super();

        this.actions = actions;

        TemplateDataProvider provider = (TemplateDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_template_actions_label"),
                new SecondaryButton(getTranslation("grid_template_actions_delete_label"), VaadinIcon.TRASH.create(), ignoredEvent -> {
                    if (!selectedIds.isEmpty()) {
                        actions.onDelete(selectedIds, selectedIds.size() > 1 ? DialogType.DELETE_TEMPLATE_ALL : DialogType.DELETE_TEMPLATE, "" + selectedIds.size());
                    }
                })
        );

        var config = GridViewConfig.<SimpleTemplate>builder()
                .gsa(gsa)
                .dataProvider(provider.getDataProvider())
                .filterSetter(s -> provider.getFilterableProvider().setFilter(s))
                .searchPlaceholder(getTranslation("grid_template_search_placeholder"))
                .emptyLabel(getTranslation("grid_template_empty_label"))
                .visibleColumns(Set.of())
                .hiddenColumns(Set.of("actions"))
                .build();

        setup(config);
    }

    @Override
    protected Class<SimpleTemplate> getItemClass() {
        return SimpleTemplate.class;
    }

    @Override
    protected void configureColumns(com.vaadin.flow.component.grid.Grid<SimpleTemplate> grid) {
        LocalDateTimeRenderer<SimpleTemplate> dateRenderer = new LocalDateTimeRenderer<>(
                d -> d.getDate().atZone(ZoneOffset.of("+03:00")).toLocalDateTime(),
                () -> DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        grid.addColumn(SimpleTemplate::getName).setHeader(getTranslation("grid_template_header_name_label")).setWidth("300px").setFlexGrow(0).setSortable(true).setResizable(true).setKey("name");
        grid.addColumn(SimpleTemplate::getText).setHeader(getTranslation("grid_template_header_text_label")).setTooltipGenerator(SimpleTemplate::getText).setSortable(true).setResizable(true).setKey("bodyText");
        grid.addColumn(dateRenderer).setHeader(getTranslation("grid_template_header_date_label")).setSortable(true).setResizable(true).setKey("createdAt");

    }

    @Override
    protected void configureActionColumn(com.vaadin.flow.component.grid.Grid<SimpleTemplate> grid) {
        grid.addComponentColumn(item -> {
            Button editButton = new IconButton(VaadinIcon.EDIT.create(), ignoredEvent -> actions.onEdit(item, DialogType.TEMPLATE_SAVED));
            Button deleteButton = new IconButton(VaadinIcon.TRASH.create(), ignoredEvent -> actions.onDelete(List.of(item.getId()), DialogType.DELETE_TEMPLATE));
            return new ButtonGroup(editButton, deleteButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("120px").setFlexGrow(0).setKey("actions");
    }

    @Override
    protected Long extractId(SimpleTemplate item) {
        return item.getId();
    }
}
