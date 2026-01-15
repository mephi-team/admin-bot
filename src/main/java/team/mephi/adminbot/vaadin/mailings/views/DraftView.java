package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.components.ButtonGroup;
import team.mephi.adminbot.vaadin.components.GridSelectActions;
import team.mephi.adminbot.vaadin.components.buttons.IconButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.grid.AbstractGridView;
import team.mephi.adminbot.vaadin.components.grid.GridViewConfig;
import team.mephi.adminbot.vaadin.mailings.dataproviders.DraftDataProvider;
import team.mephi.adminbot.vaadin.mailings.presenter.MailingsPresenter;
import team.mephi.adminbot.vaadin.service.DialogType;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

/**
 * Представление для рассылок в статусе "Черновик".
 */
public class DraftView extends AbstractGridView<SimpleMailing> {
    /**
     * Обновляет компонент статуса рассылки.
     */
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

    private final MailingsPresenter actions;

    /**
     * Конструктор представления черновиков рассылок.
     *
     * @param actions презентер для обработки действий пользователя
     */
    public DraftView(MailingsPresenter actions) {
        super();

        this.actions = actions;

        DraftDataProvider provider = (DraftDataProvider) actions.getDataProvider();
        var gsa = new GridSelectActions(getTranslation("grid_mailing_actions_label"),
                new SecondaryButton(getTranslation("grid_mailing_actions_delete_label"), VaadinIcon.TRASH.create(), ignoredEvent -> {
                    if (!selectedIds.isEmpty()) {
                        actions.onDelete(selectedIds, selectedIds.size() > 1 ? DialogType.DELETE_MAILING_ALL : DialogType.DELETE_MAILING, selectedIds.size());
                    }
                })
        );

        var config = GridViewConfig.<SimpleMailing>builder()
                .gsa(gsa)
                .dataProvider(provider.getDataProvider())
                .filterSetter(s -> provider.getFilterableProvider().setFilter(s))
                .searchPlaceholder(getTranslation("grid_mailing_search_placeholder"))
                .emptyLabel(getTranslation("grid_mailing_empty_label"))
                .visibleColumns(Set.of())
                .hiddenColumns(Set.of("actions"))
                .build();

        setup(config);
    }

    /**
     * Создает рендерер компонента для отображения статуса рассылки.
     *
     * @return рендерер компонента для статуса
     */
    private static ComponentRenderer<Span, SimpleMailing> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    @Override
    protected Class<SimpleMailing> getItemClass() {
        return SimpleMailing.class;
    }

    @Override
    protected void configureColumns(com.vaadin.flow.component.grid.Grid<SimpleMailing> grid) {
        LocalDateTimeRenderer<SimpleMailing> dateRenderer = new LocalDateTimeRenderer<>(
                d -> d.getDate().atZone(ZoneOffset.of("+03:00")).toLocalDateTime(),
                () -> DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        grid.addColumn(dateRenderer).setHeader(getTranslation("grid_mailing_header_date_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("created_at");
        grid.addColumn(SimpleMailing::getUsers).setHeader(getTranslation("grid_mailing_header_users_label")).setSortable(true).setResizable(true).setKey("filters->>'users'");
        grid.addColumn(SimpleMailing::getCohort).setHeader(getTranslation("grid_mailing_header_cohort_label")).setSortable(true).setResizable(true).setKey("filters->>'cohort'");
        grid.addColumn(SimpleMailing::getDirection).setHeader(getTranslation("grid_mailing_header_direction_label")).setSortable(true).setResizable(true).setKey("filters->>'direction'");
        grid.addColumn(SimpleMailing::getCurator).setHeader(getTranslation("grid_mailing_header_curator_label")).setSortable(true).setResizable(true).setKey("filters->>'curator'");
        grid.addColumn(SimpleMailing::getCity).setHeader(getTranslation("grid_mailing_header_city_label")).setSortable(true).setResizable(true).setKey("filters->>'city'");
        grid.addColumn(SimpleMailing::getText).setHeader(getTranslation("grid_mailing_header_text_label")).setTooltipGenerator(SimpleMailing::getText).setSortable(true).setResizable(true).setKey("description");
        grid.addColumn(createStatusComponentRenderer()).setHeader(getTranslation("grid_mailing_header_status_label")).setSortable(true).setResizable(true).setKey("status");
    }

    @Override
    protected void configureActionColumn(com.vaadin.flow.component.grid.Grid<SimpleMailing> grid) {
        grid.addComponentColumn(item -> {
            Button editButton = new IconButton(VaadinIcon.EDIT.create(), ignoredEvent -> actions.onEdit(item, DialogType.MAILING_SAVED));
            Button deleteButton = new IconButton(VaadinIcon.TRASH.create(), ignoredEvent -> actions.onDelete(List.of(item.getId()), DialogType.DELETE_MAILING));
            return new ButtonGroup(editButton, deleteButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("120px").setFlexGrow(0).setKey("actions");
    }

    @Override
    protected Long extractId(SimpleMailing item) {
        return item.getId();
    }
}
