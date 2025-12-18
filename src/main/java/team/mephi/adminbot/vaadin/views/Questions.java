package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.dto.UserQuestionDto;
import team.mephi.adminbot.vaadin.components.*;
import team.mephi.adminbot.vaadin.questions.dataproviders.QuestionDataProvider;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Route(value = "/questions", layout = DialogsLayout.class)
@PermitAll
public class Questions extends VerticalLayout {
    private static final String DELETE_TITLE = "Удалить вопрос?";
    private static final String DELETE_TEXT = "Вы действительно хотите удалить вопрос?";
    private static final String DELETE_ALL_TITLE = "Удалить вопросы?";
    private static final String DELETE_ALL_TEXT = "Вы действительно хотите удалить %d вопросов?";
    private static final String DELETE_ACTION = "Удалить";
    private static final String DELETE_MESSAGE = "Вопрос удален";
    private static final String DELETE_ALL_MESSAGE = "Удалено %d вопросов";

    private final UserConfirmDialog dialogDelete;

    private final QuestionDataProvider provider;
    private List<Long> selectedIds;

    public Questions(QuestionDataProvider provider) {
        this.provider = provider;

        this.dialogDelete = new UserConfirmDialog(
                DELETE_TITLE, DELETE_TEXT, DELETE_ACTION,
                DELETE_ALL_TITLE, DELETE_ALL_TEXT,
                null
        );

        add(new H1("Вопросы"));

        var gsa = new GridSelectActions(
                new Button("Удалить вопросы", VaadinIcon.TRASH.create(), e -> {
                    if (!selectedIds.isEmpty()) {
                        onDelete(selectedIds);
                    }
                })
        );

        setSizeFull();

        Grid<UserQuestionDto> grid = new Grid<>(UserQuestionDto.class, false);

        grid.addColumn(UserQuestionDto::getQuestion).setHeader("Вопрос").setSortable(true).setKey("question");
        grid.addColumn(UserQuestionDto::getDate).setHeader("Дата").setSortable(true).setKey("date");
        grid.addColumn(UserQuestionDto::getUser).setHeader("Автор").setSortable(true).setKey("author");
        grid.addColumn(UserQuestionDto::getRole).setHeader("Роль").setSortable(true).setKey("role");
        grid.addColumn(UserQuestionDto::getDirection).setHeader("Направление").setSortable(true).setKey("direction");
        grid.addColumn(UserQuestionDto::getAnswer).setHeader("Ответ").setSortable(true).setKey("answer");

        grid.addComponentColumn(item -> {
            Span group = new Span();
            Button responseButton = new Button("Ответить",e -> System.out.println(item));
            Button chatButton = new Button(new Icon(VaadinIcon.CHAT),e -> UI.getCurrent().navigate(Dialogs.class, new QueryParameters(Map.of("userId", List.of("" + item.getId())))));
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), e -> onDelete(List.of(item.getId())));
            group.add(responseButton, chatButton, deleteButton);
            return group;
        }).setHeader("Действия").setWidth("220px").setFlexGrow(0).setKey("action");

        grid.setDataProvider(provider.getDataProvider());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addSelectionListener(selection -> {
            selectedIds = selection.getAllSelectedItems().stream().map(UserQuestionDto::getId).toList();
            gsa.setCount(selectedIds.size());
        });

        var searchField = new SearchField("Найти вопрос");
        searchField.addValueChangeListener(e -> provider.getFilterableProvider().setFilter(e.getValue()));

        var settingsBtn = new GridSettingsButton();
        var settingsPopover = new GridSettingsPopover(grid, Set.of());
        settingsPopover.setTarget(settingsBtn);

        add(new SearchFragment(searchField, settingsBtn), gsa, grid);
    }

    private void onDelete(List<Long> selectedIds) {
        dialogDelete.setCount(selectedIds.size());
        dialogDelete.setOnConfirm(() -> {
            provider.deleteAllById(selectedIds);
            provider.refresh();
            if (selectedIds.size() > 1) {
                Notification.show(String.format(DELETE_ALL_MESSAGE, selectedIds.size()), 3000, Notification.Position.TOP_END);
            } else {
                Notification.show(DELETE_MESSAGE, 3000, Notification.Position.TOP_END);
            }
        });
        dialogDelete.open();
    }
}
