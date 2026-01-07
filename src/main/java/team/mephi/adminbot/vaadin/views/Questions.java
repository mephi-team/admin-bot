package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.questions.components.AnswerDialog;
import team.mephi.adminbot.vaadin.questions.components.AnswerDialogFactory;
import team.mephi.adminbot.vaadin.questions.dataproviders.QuestionDataProvider;
import team.mephi.adminbot.vaadin.questions.dataproviders.QuestionDataProviderFactory;
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
    private final NotificationService notificationService;
    private final SimpleConfirmDialog dialogDelete;
    private final AnswerDialog answerDialog;

    private final QuestionDataProvider provider;
    private List<Long> selectedIds;

    public Questions(QuestionDataProviderFactory factory, AnswerDialogFactory dialogFactory, NotificationService notificationService) {
        this.answerDialog = dialogFactory.create();
        this.provider = factory.createDataProvider();
        this.notificationService = notificationService;

        this.dialogDelete = new SimpleConfirmDialog(
                "dialog_delete_question_title", "dialog_delete_question_text", "dialog_delete_question_action",
                "dialog_delete_question_all_title", "dialog_delete_question_all_text"
        );

        add(new H1(getTranslation("page_question_title")));

        var gsa = new GridSelectActions(getTranslation("grid_question_actions_label"),
                new Button(getTranslation("grid_question_actions_delete_label"), VaadinIcon.TRASH.create(), e -> {
                    if (!selectedIds.isEmpty()) {
                        onDelete(selectedIds);
                    }
                })
        );

        setSizeFull();

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
        grid.addColumn(SimpleQuestion::getAnswer).setHeader(getTranslation("grid_question_header_answer_label")).setResizable(true).setKey("answers");

        grid.addComponentColumn(item -> {
            Span group = new Span();
            Button responseButton = new Button(getTranslation("grid_question_action_answer_label"), e -> onAnswer(item));
            Button chatButton = new Button(VaadinIcon.CHAT.create(),e -> UI.getCurrent().navigate(Dialogs.class, QueryParameters.of("userId", "" + item.getAuthorId())));
            Button deleteButton = new Button(VaadinIcon.TRASH.create(), e -> onDelete(List.of(item.getId())));
            group.add(responseButton, chatButton, deleteButton);
            return group;
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("220px").setFlexGrow(0).setKey("actions");

        grid.setDataProvider(provider.getDataProvider());
        GridMultiSelectionModel<?> selectionModel = (GridMultiSelectionModel<?>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.setSelectionColumnFrozen(true);
//        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addSelectionListener(selection -> {
            selectedIds = selection.getAllSelectedItems().stream().map(SimpleQuestion::getId).toList();
            gsa.setCount(selectedIds.size());
        });
        provider.getFilterableProvider().addDataProviderListener(e -> {
            grid.deselectAll();
        });

        var searchField = new SearchField(getTranslation("grid_question_search_placeholder"));
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of(), Set.of("actions"));
        settingsPopover.setTarget(settingsBtn);

        var downloadBtn = new Button(VaadinIcon.DOWNLOAD_ALT.create());

        add(new SearchFragment(searchField, new Span(settingsBtn, downloadBtn)), gsa, grid);
    }

    private void onAnswer(SimpleQuestion question) {
        answerDialog.showDialogForEdit(question, (editedItem) -> {
            if (editedItem != null) {
                var savedAnswer = provider.saveAnswer(editedItem);
                provider.getDataProvider().refreshItem(savedAnswer);
                notificationService.showNotification(NotificationType.EDIT, "notification_answer_send", ""+question.getId());
            }
        });
    }

    private void onDelete(List<Long> selectedIds) {
        dialogDelete.showForConfirm(selectedIds.size(), () -> {
            provider.deleteAllById(selectedIds);
            provider.getDataProvider().refreshAll();
            notificationService.showNotification(NotificationType.DELETE, selectedIds.size() > 1 ? "notification_question_delete_all" : "notification_question_delete", ""+selectedIds.size());
        });
    }
}
