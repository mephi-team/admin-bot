package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.questions.dataproviders.QuestionDataProvider;
import team.mephi.adminbot.vaadin.questions.dataproviders.QuestionDataProviderFactory;
import team.mephi.adminbot.vaadin.service.DialogService;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.service.NotificationService;
import team.mephi.adminbot.vaadin.service.NotificationType;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Route(value = "/questions", layout = DialogsLayout.class)
@PermitAll
public class Questions extends VerticalLayout {
    private final DialogService<?> dialogService;
    private final NotificationService notificationService;

    private final QuestionDataProvider provider;
    private List<Long> selectedIds;

    public Questions(QuestionDataProviderFactory providerFactory, DialogService<?> dialogService, NotificationService notificationService) {
        this.provider = providerFactory.createDataProvider();
        this.dialogService = dialogService;
        this.notificationService = notificationService;

        add(new H1(getTranslation("page_question_title")));

        var gsa = new GridSelectActions(getTranslation("grid_question_actions_label"),
                new SecondaryButton(getTranslation("grid_question_actions_delete_label"), VaadinIcon.TRASH.create(), e -> {
                    if (!selectedIds.isEmpty()) {
                        onDelete(selectedIds);
                    }
                })
        );

        setSizeFull();
        getElement().getStyle().set("padding-inline", "53px 120px");
        LocalDateTimeRenderer<SimpleQuestion> dateRenderer = new LocalDateTimeRenderer<>(
                d -> Objects.isNull(d.getDate()) ? null : d.getDate().atZone(ZoneOffset.of("+03:00")).toLocalDateTime(),
                () -> DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        Grid<SimpleQuestion> grid = new Grid<>(SimpleQuestion.class, false);
        grid.addColumn(SimpleQuestion::getText).setHeader(getTranslation("grid_question_header_question_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("text");
        grid.addColumn(dateRenderer).setHeader(getTranslation("grid_question_header_date_label")).setSortable(true).setResizable(true).setKey("createdAt");
        grid.addColumn(SimpleQuestion::getAuthor).setHeader(getTranslation("grid_question_header_author_label")).setSortable(true).setResizable(true).setKey("user");
        grid.addColumn(SimpleQuestion::getRole).setHeader(getTranslation("grid_question_header_role_label")).setSortable(true).setResizable(true).setKey("role");
        grid.addColumn(SimpleQuestion::getDirection).setHeader(getTranslation("grid_question_header_direction_label")).setSortable(true).setResizable(true).setKey("direction");
        grid.addColumn(SimpleQuestion::getAnswer).setHeader(getTranslation("grid_question_header_answer_label")).setTooltipGenerator(SimpleQuestion::getAnswer).setResizable(true).setKey("answers");

        grid.addComponentColumn(item -> {
            Button responseButton = new TextButton(getTranslation("grid_question_action_answer_label"), e -> onAnswer(item));
            Button chatButton = new IconButton(VaadinIcon.CHAT.create(), e -> UI.getCurrent().navigate(Dialogs.class, QueryParameters.of("userId", "" + item.getAuthorId())));
            Button deleteButton = new IconButton(VaadinIcon.TRASH.create(), e -> onDelete(List.of(item.getId())));
            return new ButtonGroup(responseButton, chatButton, deleteButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("210px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        GridMultiSelectionModel<?> selectionModel = (GridMultiSelectionModel<?>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);
//        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addSelectionListener(selection -> {
            selectedIds = selection.getAllSelectedItems().stream().map(SimpleQuestion::getId).toList();
            gsa.setCount(selectedIds.size());
        });
        grid.setEmptyStateText(getTranslation("grid_question_empty_label"));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeName("neo");

        provider.getFilterableProvider().addDataProviderListener(e -> {
            grid.deselectAll();
        });

        var searchField = new SearchField(getTranslation("grid_question_search_placeholder"));
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new IconButton(VaadinIcon.COG.create());
        var settingsPopover = new GridSettingsPopover(grid, Set.of(), Set.of("actions"));
        settingsPopover.setTarget(settingsBtn);

        var downloadBtn = new IconButton(VaadinIcon.DOWNLOAD_ALT.create(), e -> {
        });

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);
    }

    private void onAnswer(SimpleQuestion question) {
        dialogService.showDialog(question, DialogType.ANSWER_SEND, (editedItem) -> {
            if (editedItem != null) {
                var savedAnswer = provider.saveAnswer((SimpleQuestion) editedItem);
                provider.getDataProvider().refreshItem(savedAnswer);
                notificationService.showNotification(NotificationType.EDIT, DialogType.ANSWER_SEND.getNotificationKey(), question.getId());
            }
        });
    }

    private void onDelete(List<Long> selectedIds) {
        dialogService.showConfirmDialog(selectedIds.size(), selectedIds.size() > 1 ? DialogType.DELETE_QUESTION_ALL : DialogType.DELETE_QUESTION, VaadinIcon.TRASH.create(), (ignore) -> {
            provider.deleteAllById(selectedIds);
            provider.getDataProvider().refreshAll();
            notificationService.showNotification(NotificationType.DELETE, selectedIds.size() > 1 ? DialogType.DELETE_QUESTION_ALL.getNotificationKey() : DialogType.DELETE_QUESTION.getNotificationKey(), selectedIds.size());
        });
    }
}
