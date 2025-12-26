package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.components.SimpleConfirmDialog;
import team.mephi.adminbot.vaadin.components.UserCountBadge;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.components.UserEditorDialog;
import team.mephi.adminbot.vaadin.users.components.UserEditorDialogFactory;
import team.mephi.adminbot.vaadin.users.service.UserCountService;
import team.mephi.adminbot.vaadin.users.service.UserViewCallback;
import team.mephi.adminbot.vaadin.users.service.UsersPresenter;
import team.mephi.adminbot.vaadin.users.service.UsersPresenterFactory;
import team.mephi.adminbot.vaadin.users.tabs.UserTabProvider;

import java.util.*;

@Route("/users")
@RolesAllowed("ADMIN")
public class Users extends VerticalLayout implements UserViewCallback {
    private static final String BLOCK_TITLE = "Блокировать пользователя?";
    private static final String BLOCK_TEXT = "Вы действительно хотите заблокировать пользователя?";
    private static final String BLOCK_ALL_TITLE = "Блокировать пользователей?";
    private static final String BLOCK_ALL_TEXT = "Вы действительно хотите заблокировать %d пользователей?";
    private static final String BLOCK_ACTION = "Блокировать";

    private static final String ACCEPT_TITLE = "Утвердить кандидата?";
    private static final String ACCEPT_TEXT = "Вы действительно хотите утвердить кандидата?";
    private static final String ACCEPT_ALL_TITLE = "Утвердить кандидатов?";
    private static final String ACCEPT_ALL_TEXT = "Вы действительно хотите утвердить %d кандидатов?";
    private static final String ACCEPT_ACTION = "Утвердить";

    private static final String REJECT_TITLE = "Отклонить кандидата?";
    private static final String REJECT_TEXT = "Вы действительно хотите отклонить кандидата?";
    private static final String REJECT_ALL_TITLE = "Отклонить кандидатов?";
    private static final String REJECT_ALL_TEXT = "Вы действительно хотите отклонить %d кандидатов?";
    private static final String REJECT_ACTION = "Отклонить";

    // --- Константы текстов ---
    private static final String BLOCK_MESSAGE = "Пользователь заблокирован";
    private static final String BLOCK_ALL_MESSAGE = "Заблокировано %d пользователей";

    private static final String ACCEPT_MESSAGE = "Информация о приеме отправлена";
    private static final String ACCEPT_ALL_MESSAGE = "Информация о приеме отправлена %d кандидатам";

    private static final String REJECT_MESSAGE = "Информация об отказе отправлена кандидату";
    private static final String REJECT_ALL_MESSAGE = "Информация об отказе отправлена %d кандидатам";

    private static final String USER_CREATED = "Пользователь добавлен";
    private static final String USER_SAVED = "Пользователь сохранён";


    private final UserEditorDialog editorDialog;
    private final SimpleConfirmDialog dialogBlock;
    private final SimpleConfirmDialog dialogAccept;
    private final SimpleConfirmDialog dialogReject;

    private final TabSheet tabSheet = new TabSheet();
    private final List<String> rolesInOrder = new ArrayList<>();
    private final Map<String, UserActions> actions = new HashMap<>();

    private static final UserActions NO_OP_ACTIONS = new UserActions() {
        @Override public void onCreate(String role) {}
        @Override public void onView(Long id) {}
        @Override public void onEdit(Long id) {}
        @Override public void onDelete(List<Long> ids) {}
        @Override public void onAccept(List<Long> ids) {}
        @Override public void onReject(List<Long> ids) {}
    };

    public Users(
            List<UserTabProvider> tabProviders,
            UsersPresenterFactory presenterFactory, // ← новый фабричный сервис
            UserEditorDialogFactory dialogFactory,
            UserCountService userCountService
    ) {
        this.editorDialog = dialogFactory.create();

        this.dialogBlock = new SimpleConfirmDialog(
                BLOCK_TITLE, BLOCK_TEXT, BLOCK_ACTION,
                BLOCK_ALL_TITLE, BLOCK_ALL_TEXT,
                null
        );
        this.dialogAccept = new SimpleConfirmDialog(
                ACCEPT_TITLE, ACCEPT_TEXT, ACCEPT_ACTION,
                ACCEPT_ALL_TITLE, ACCEPT_ALL_TEXT,
                null
        );
        this.dialogReject = new SimpleConfirmDialog(
                REJECT_TITLE, REJECT_TEXT, REJECT_ACTION,
                REJECT_ALL_TITLE, REJECT_ALL_TEXT,
                null
        );

        setSizeFull();
        tabSheet.setSizeFull();
        add(createHeader(), tabSheet, editorDialog);

        tabProviders.sort(Comparator.comparingInt(UserTabProvider::getPosition));

        // Создаём вкладки
        for (var provider : tabProviders) {
            var tabId = provider.getTabId();
            var dataProvider = presenterFactory.createDataProvider(tabId);
            var presenter = new UsersPresenter(dataProvider, this);
            var content = provider.createTabContent(dataProvider, presenter);

            rolesInOrder.add(tabId);
            actions.put(tabId, presenter);

            var userCount = userCountService.getAllCounts().getOrDefault(provider.getTabId(), 0L);
            Span tabContent = new Span(new Span(provider.getTabLabel()), new UserCountBadge(userCount));
            tabSheet.add(new Tab(tabContent), content, provider.getPosition());
        }
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1("Пользователи"));

        var primaryButton = new Button("Добавить пользователя", new Icon(VaadinIcon.PLUS), e -> {
            getCurrentAction().onCreate(getCurrentRole());
        });
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Div buttons = new Div(new Button("Загрузить из файла", new Icon(VaadinIcon.FILE_ADD)), primaryButton);
        buttons.getElement().getStyle().set("display", "flex");
        buttons.getElement().getStyle().set("gap", "24px");
        top.addToEnd(buttons);
        return top;
    }

    private String getCurrentRole() {
        var selectedTab = tabSheet.getSelectedIndex();
        if (selectedTab > -1) {
            return rolesInOrder.get(selectedTab);
        }
        return "visitor";
    }

    private UserActions getCurrentAction() {
        return actions.getOrDefault(getCurrentRole(), NO_OP_ACTIONS);
    }

    @Override
    public void setOnSaveCallback(SerializableRunnable callback) {
        editorDialog.setOnSaveCallback(callback);
    }

    @Override
    public SimpleUser getEditedItem() {
        return editorDialog.getEditedUser();
    }

    @Override
    public void showDialogForView(SimpleUser user) {
        editorDialog.openForView(user);
    }

    @Override
    public void showDialogForEdit(SimpleUser user) {
        editorDialog.openForEdit(user);
    }

    @Override
    public void showDialogForNew(String role) {
        editorDialog.openForNew(role);
    }

    @Override
    public void confirmDelete(List<Long> ids, Runnable onConfirm) {
        dialogBlock.showForConfirm(ids.size(), onConfirm);
    }

    @Override
    public void showNotificationForNew() {
        Notification.show(USER_CREATED, 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForEdit(Long id) {
        Notification.show(USER_SAVED, 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForDelete(List<Long> ids) {
        Notification.show(makeNotification(BLOCK_MESSAGE, BLOCK_ALL_MESSAGE, ids.size()), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void confirmAccept(List<Long> ids, Runnable onConfirm) {
        dialogAccept.showForConfirm(ids.size(), onConfirm);
    }

    @Override
    public void confirmReject(List<Long> ids, Runnable onConfirm) {
        dialogReject.showForConfirm(ids.size(), onConfirm);
    }

    @Override
    public void showNotificationForAccept(List<Long> ids) {
        Notification.show(makeNotification(ACCEPT_MESSAGE, ACCEPT_ALL_MESSAGE, ids.size()), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForReject(List<Long> ids) {
        Notification.show(makeNotification(REJECT_MESSAGE, REJECT_ALL_MESSAGE, ids.size()), 3000, Notification.Position.TOP_END);
    }

    private String makeNotification(String single, String plural, int count) {
        if (count > 1) {
            return String.format(plural, count);
        }
        return single;
    }
}
