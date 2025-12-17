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
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.vaadin.components.UserConfirmDialog;
import team.mephi.adminbot.vaadin.components.UserCountBadge;
import team.mephi.adminbot.vaadin.users.components.RoleService;
import team.mephi.adminbot.vaadin.users.components.UserEditorDialog;
import team.mephi.adminbot.vaadin.users.dataproviders.UserDataProvider;
import team.mephi.adminbot.vaadin.users.service.UserCountService;
import team.mephi.adminbot.vaadin.users.service.UsersPresenter;
import team.mephi.adminbot.vaadin.users.actions.UserActions;
import team.mephi.adminbot.vaadin.users.tabs.UserTabProvider;

import java.util.*;

@Route("/users")
@RolesAllowed("ADMIN")
public class Users extends VerticalLayout {
    private final String BLOCK_TITLE = "Блокировать пользователя?";
    private final String BLOCK_TEXT = "Вы действительно хотите заблокировать пользователя?";
    private final String BLOCK_MESSAGE = "Пользователь заблокирован";
    private final String BLOCK_ALL_TITLE = "Блокировать пользователей?";
    private final String BLOCK_ALL_TEXT = "Вы действительно хотите заблокировать %d пользователей?";
    private final String BLOCK_ALL_MESSAGE = "Заблокировано %d пользователей";
    private final String BLOCK_ACTION = "Блокировать";
    private final String ACCEPT_TITLE = "Утвердить кандидата?";
    private final String ACCEPT_TEXT = "Вы действительно хотите утвердить кандидата?";
    private final String ACCEPT_MESSAGE = "Информация о приеме отправлена";
    private final String ACCEPT_ALL_TITLE = "Утвердить кандидатов?";
    private final String ACCEPT_ALL_TEXT = "Вы действительно хотите утвердить %d кандидатов?";
    private final String ACCEPT_ALL_MESSAGE = "Информация о приеме отправлена %d кандидатам";
    private final String ACCEPT_ACTION = "Утвердить";
    private final String REJECT_TITLE = "Отклонить кандидата?";
    private final String REJECT_TEXT = "Вы действительно хотите отклонить кандидата?";
    private final String REJECT_MESSAGE = "Информация об отказе отправлена кандидату";
    private final String REJECT_ALL_TITLE = "Отклонить кандидатов?";
    private final String REJECT_ALL_TEXT = "Вы действительно хотите отклонить %d кандидатов?";
    private final String REJECT_ALL_MESSAGE = "Информация об отказе отправлена %d кандидатам";
    private final String REJECT_ACTION = "Отклонить";

    private final UserEditorDialog editorDialog;
    private final UserConfirmDialog dialogBlock;
    private final UserConfirmDialog dialogAccept;
    private final UserConfirmDialog dialogReject;
    private final TabSheet tabSheet = new TabSheet();
    private final List<String> rolesInOrder = new ArrayList<>();
    private final Map<String, UserDataProvider> dataProviders = new HashMap<>();

    public Users(
            List<UserTabProvider> tabProviders,
            UsersPresenter presenter,
            RoleService roleService,
            UserCountService userCountService
    ) {
        this.editorDialog = new UserEditorDialog(roleService);
        this.dialogBlock = new UserConfirmDialog(BLOCK_TITLE, BLOCK_TEXT, BLOCK_ACTION, BLOCK_ALL_TITLE, BLOCK_ALL_TEXT, null);
        this.dialogAccept = new UserConfirmDialog(ACCEPT_TITLE, ACCEPT_TEXT, ACCEPT_ACTION, ACCEPT_ALL_TITLE, ACCEPT_ALL_TEXT, null);
        this.dialogReject = new UserConfirmDialog(REJECT_TITLE, REJECT_TEXT, REJECT_ACTION, REJECT_ALL_TITLE, REJECT_ALL_TEXT, null);
        setSizeFull();
        tabSheet.setSizeFull();
        add(createHeader(), tabSheet, editorDialog);
        tabProviders.sort(Comparator.comparingInt(UserTabProvider::getPosition));
        // Создаём вкладки
        for (var provider : tabProviders) {
            var tabId = provider.getTabId();
            var dataProvider = presenter.createDataProvider(tabId);
            var actions = createActions(tabId, dataProvider);
            var content = provider.createTabContent(actions);
            rolesInOrder.add(tabId);
            dataProviders.put(tabId, dataProvider);
            var userCount = userCountService.getAllCounts().getOrDefault(provider.getTabId(), 0L);
            Span tabContent = new Span(new Span(provider.getTabLabel()), new UserCountBadge(userCount));
            tabSheet.add(new Tab(tabContent), content, provider.getPosition());
        }
    }

    private UserActions createActions(String role, UserDataProvider dataProvider) {
        return new UserActions() {
            @Override
            public void onView(Long id) {
                dataProvider.findUserById(id).ifPresent(editorDialog::openForView);
            }
            @Override
            public void onEdit(Long id) {
                dataProvider.findUserById(id).ifPresent(u -> {
                    editorDialog.openForEdit(u);
                    editorDialog.setOnSaveCallback(() -> {
                        dataProvider.save(editorDialog.getEditedUser());
                        dataProvider.refresh();
                    });
                });
            }
            @Override
            public void onDelete(List<Long> ids) {
                dialogBlock.setCount(ids.size());
                dialogBlock.setOnConfirm(() -> {
                    dataProvider.deleteAllById(ids);
                    dataProvider.refresh();
                    if (ids.size() == 1)
                        Notification.show(BLOCK_MESSAGE, 3000, Notification.Position.TOP_END);
                    else if (ids.size() > 1) {
                        Notification.show(String.format(BLOCK_ALL_MESSAGE, ids.size()), 3000, Notification.Position.TOP_END);
                    }
                });
                dialogBlock.open();
            }
            @Override
            public void onAccept(List<Long> ids) {
                dialogAccept.setCount(ids.size());
                dialogAccept.setOnConfirm(() -> {
                    if (ids.size() == 1)
                        Notification.show(ACCEPT_MESSAGE, 3000, Notification.Position.TOP_END);
                    else if (ids.size() > 1) {
                        Notification.show(String.format(ACCEPT_ALL_MESSAGE, ids.size()), 3000, Notification.Position.TOP_END);
                    }
                });
                dialogAccept.open();
            }
            @Override
            public void onReject(List<Long> ids) {
                dialogReject.setCount(ids.size());
                dialogReject.setOnConfirm(() -> {
                    if (ids.size() == 1)
                        Notification.show(REJECT_MESSAGE, 3000, Notification.Position.TOP_END);
                    else if (ids.size() > 1) {
                        Notification.show(String.format(REJECT_ALL_MESSAGE, ids.size()), 3000, Notification.Position.TOP_END);
                    }
                });
                dialogReject.open();
            }
        };
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1("Пользователи"));
        var primaryButton = new Button("Добавить пользователя", new Icon(VaadinIcon.PLUS), e -> {
            editorDialog.openForNew(getCurrentRole());
            editorDialog.setOnSaveCallback(() -> {
                var dataProvider = dataProviders.get(getCurrentRole());
                dataProvider.save(editorDialog.getEditedUser());
                dataProvider.refresh();
            });
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
}
