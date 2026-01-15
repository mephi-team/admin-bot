package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.dto.SimpleQuestion;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.vaadin.components.ButtonGroup;
import team.mephi.adminbot.vaadin.components.GridSelectActions;
import team.mephi.adminbot.vaadin.components.buttons.IconButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.components.buttons.TextButton;
import team.mephi.adminbot.vaadin.components.grid.AbstractGridView;
import team.mephi.adminbot.vaadin.components.grid.GridViewConfig;
import team.mephi.adminbot.vaadin.components.layout.DialogsLayout;
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

/**
 * Представление страницы вопросов с возможностью отвечать на вопросы и удалять их.
 * Доступно всем аутентифицированным пользователям.
 */
@Route(value = "/questions", layout = DialogsLayout.class)
@PermitAll
public class Questions extends AbstractGridView<SimpleQuestion> {
    private final DialogService<?> dialogService;
    private final NotificationService notificationService;
    private final AuthService authService;

    private final QuestionDataProvider provider;

    /**
     * Конструктор для создания представления страницы вопросов.
     *
     * @param providerFactory     фабрика для создания провайдера данных вопросов.
     * @param dialogService       сервис для отображения диалогов.
     * @param notificationService сервис для отображения уведомлений.
     * @param authService         сервис для аутентификации и авторизации пользователей.
     */
    public Questions(QuestionDataProviderFactory providerFactory, DialogService<?> dialogService, NotificationService notificationService, AuthService authService) {
        this.provider = providerFactory.createDataProvider();
        this.dialogService = dialogService;
        this.notificationService = notificationService;
        this.authService = authService;

        add(new H1(getTranslation("page_question_title")));

        var gsa = new GridSelectActions(getTranslation("grid_question_actions_label"),
                authService.isAdmin() ? new SecondaryButton(getTranslation("grid_question_actions_delete_label"), VaadinIcon.TRASH.create(), ignoredEvent -> {
                    if (!selectedIds.isEmpty()) {
                        onDelete(selectedIds);
                    }
                }) : new Span()
        );

        getElement().getStyle().set("padding", "16px 120px 16px 53px");

        var config = GridViewConfig.<SimpleQuestion>builder()
                .gsa(gsa)
                .dataProvider(provider.getDataProvider())
                .filterSetter(s -> provider.getFilterableProvider().setFilter(s))
                .searchPlaceholder(getTranslation("grid_question_search_placeholder"))
                .emptyLabel(getTranslation("grid_question_empty_label"))
                .visibleColumns(Set.of())
                .hiddenColumns(Set.of("actions"))
                .build();

        setup(config);
    }

    /**
     * Обработка ответа на вопрос.
     *
     * @param question вопрос, на который нужно ответить.
     */
    private void onAnswer(SimpleQuestion question) {
        dialogService.showDialog(question, DialogType.ANSWER_SEND, (editedItem) -> {
            if (editedItem != null) {
                var savedAnswer = provider.saveAnswer((SimpleQuestion) editedItem);
                provider.getDataProvider().refreshItem(savedAnswer);
                notificationService.showNotification(NotificationType.EDIT, DialogType.ANSWER_SEND.getNotificationKey(), question.getId());
            }
        });
    }

    /**
     * Обработка удаления вопросов.
     *
     * @param selectedIds список идентификаторов вопросов для удаления.
     */
    private void onDelete(List<Long> selectedIds) {
        dialogService.showConfirmDialog(selectedIds.size(), selectedIds.size() > 1 ? DialogType.DELETE_QUESTION_ALL : DialogType.DELETE_QUESTION, VaadinIcon.TRASH.create(), (ignore) -> {
            provider.deleteAllById(selectedIds);
            provider.getDataProvider().refreshAll();
            notificationService.showNotification(NotificationType.DELETE, selectedIds.size() > 1 ? DialogType.DELETE_QUESTION_ALL.getNotificationKey() : DialogType.DELETE_QUESTION.getNotificationKey(), selectedIds.size());
        });
    }

    @Override
    protected Class<SimpleQuestion> getItemClass() {
        return SimpleQuestion.class;
    }

    @Override
    protected void configureColumns(com.vaadin.flow.component.grid.Grid<SimpleQuestion> grid) {
        LocalDateTimeRenderer<SimpleQuestion> dateRenderer = new LocalDateTimeRenderer<>(
                d -> Objects.isNull(d.getDate()) ? null : d.getDate().atZone(ZoneOffset.of("+03:00")).toLocalDateTime(),
                () -> DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        grid.addColumn(SimpleQuestion::getText).setHeader(getTranslation("grid_question_header_question_label")).setSortable(true).setResizable(true).setFrozen(true)
                .setAutoWidth(true).setFlexGrow(0).setKey("text");
        grid.addColumn(dateRenderer).setHeader(getTranslation("grid_question_header_date_label")).setSortable(true).setResizable(true).setKey("createdAt");
        grid.addColumn(SimpleQuestion::getAuthor).setHeader(getTranslation("grid_question_header_author_label")).setSortable(true).setResizable(true).setKey("user");
        grid.addColumn(SimpleQuestion::getRole).setHeader(getTranslation("grid_question_header_role_label")).setSortable(true).setResizable(true).setKey("role");
        grid.addColumn(SimpleQuestion::getDirection).setHeader(getTranslation("grid_question_header_direction_label")).setSortable(true).setResizable(true).setKey("direction");
        grid.addColumn(SimpleQuestion::getAnswer).setHeader(getTranslation("grid_question_header_answer_label")).setTooltipGenerator(SimpleQuestion::getAnswer).setResizable(true).setKey("answers");
    }

    @Override
    protected void configureActionColumn(com.vaadin.flow.component.grid.Grid<SimpleQuestion> grid) {
        grid.addComponentColumn(item -> {
            Component responseButton = item.getAnswer().isEmpty() ? new TextButton(getTranslation("grid_question_action_answer_label"), ignoredEvent -> onAnswer(item)) : new Span();
            Button chatButton = new IconButton(VaadinIcon.CHAT.create(), ignoredEvent -> UI.getCurrent().navigate(Dialogs.class, QueryParameters.of("userId", "" + item.getAuthorId())));
            Button deleteButton = new IconButton(VaadinIcon.TRASH.create(), ignoredEvent -> onDelete(List.of(item.getId())));
            if (authService.isAdmin())
                return new ButtonGroup(responseButton, chatButton, deleteButton);
            return new ButtonGroup(responseButton);
        }).setHeader(getTranslation("grid_header_actions_label")).setWidth("210px").setFlexGrow(0).setKey("actions");
    }

    @Override
    protected Long extractId(SimpleQuestion item) {
        return item.getId();
    }
}
