package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.components.SimpleConfirmDialog;
import team.mephi.adminbot.vaadin.components.UserCountBadge;
import team.mephi.adminbot.vaadin.users.components.*;
import team.mephi.adminbot.vaadin.users.presenter.*;
import team.mephi.adminbot.vaadin.users.service.*;
import team.mephi.adminbot.vaadin.users.tabs.UserTabProvider;

import java.util.*;

@Route("/users")
@RolesAllowed("ADMIN")
public class Users extends VerticalLayout implements StudentViewCallback, TutorViewCallback, BeforeEnterObserver {
    private final FileUploadDialog fileUploadDialog;
    private final UserEditorDialog editorDialog;
    private final TutoringDialog tutoringDialog;
    private final BlockDialog blockDialog;
    private final SimpleConfirmDialog dialogDelete;
    private final SimpleConfirmDialog dialogAccept;
    private final SimpleConfirmDialog dialogReject;
    private final SimpleConfirmDialog dialogExpel;

    private final TabSheet tabSheet = new TabSheet();
    private final List<String> rolesInOrder = new ArrayList<>();
    private final Map<String, CRUDActions> actions = new HashMap<>();

    private String currentTab;

    public Users(
            List<UserTabProvider> tabProviders,
            UsersPresenterFactory presenterFactory,
            FileUploadDialogFactory uploaderFactory,
            UserEditorDialogFactory dialogFactory,
            UserCountService userCountService,
            TutoringDialogFactory tutoringDialogFactory,
            BlockDialogFactory blockDialogFactory
    ) {
        this.fileUploadDialog = uploaderFactory.create();
        this.editorDialog = dialogFactory.create();
        this.tutoringDialog = tutoringDialogFactory.create();
        this.blockDialog = blockDialogFactory.create();

        this.dialogDelete = new SimpleConfirmDialog(
                "dialog_delete_users_title", "dialog_delete_users_text", "dialog_delete_users_action",
                "dialog_delete_users_all_title", "dialog_delete_users_all_text"
        );
        this.dialogAccept = new SimpleConfirmDialog(
                "dialog_accept_users_title", "dialog_accept_users_text", "dialog_accept_users_action",
                "dialog_accept_users_all_title", "dialog_accept_users_all_text"
        );
        this.dialogReject = new SimpleConfirmDialog(
                "dialog_reject_users_title", "dialog_reject_users_text", "dialog_reject_users_action",
                "dialog_reject_users_all_title", "dialog_reject_users_all_text"
        );
        this.dialogExpel = new SimpleConfirmDialog(
                "dialog_expel_users_title", "dialog_expel_users_text", "dialog_expel_users_action",
                "dialog_expel_users_all_title", "dialog_expel_users_all_text"
        );

        setSizeFull();
        tabSheet.setSizeFull();
        add(createHeader(), tabSheet, editorDialog);

        tabProviders.sort(Comparator.comparingInt(UserTabProvider::getPosition));

        // Создаём вкладки
        for (var provider : tabProviders) {
            var tabId = provider.getTabId();
            var presenter = presenterFactory.createPresenter(tabId, this);
            var content = provider.createTabContent(presenter);

            rolesInOrder.add(tabId);
            actions.put(tabId, presenter);

            var userCount = userCountService.getAllCounts().getOrDefault(provider.getTabId(), 0L);
            Span tabContent = new Span(new Span(getTranslation(provider.getTabLabel())), new UserCountBadge(userCount));
            tabSheet.add(new Tab(tabContent), content, provider.getPosition());
        }
        History history = UI.getCurrent().getPage().getHistory();
        tabSheet.addSelectedChangeListener(e -> {
            var selectedTab = tabSheet.getSelectedIndex();
            if (selectedTab > -1) {
                currentTab = rolesInOrder.get(selectedTab);
                history.replaceState(null,  "users?tab=" + currentTab);
            }
        });
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1(getTranslation("page_users_title")));

        var secondaryButton = new Button(getTranslation("page_users_create_from_file_button"), new Icon(VaadinIcon.FILE_ADD), e -> {
            fileUploadDialog.open();
        });
        var primaryButton = new Button(getTranslation("page_users_create_user_button"), new Icon(VaadinIcon.PLUS), e -> {
            getCurrentAction().onCreate(getCurrentRole());
        });
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Div buttons = new Div(secondaryButton, primaryButton);
        buttons.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.MEDIUM);
        top.addToEnd(buttons);
        return top;
    }

    private String getCurrentRole() {
        return currentTab;
    }

    private CRUDActions getCurrentAction() {
        return actions.get(getCurrentRole());
    }

    @Override
    public void showDialogForView(SimpleUser user) {
        editorDialog.setHeaderTitle("dialog_users_view_title");
        editorDialog.openForView(user);
    }

    @Override
    public void showDialogForEdit(Object user, SerializableConsumer<?> callback) {
        editorDialog.setHeaderTitle("dialog_users_edit_title");
        editorDialog.openForEdit((SimpleUser) user, (SerializableConsumer<SimpleUser>) callback);
    }

    @Override
    public void showDialogForNew(String role, SerializableConsumer<?> callback) {
        editorDialog.setHeaderTitle("dialog_users_new_title");
        editorDialog.openForNew(role, (SerializableConsumer<SimpleUser>) callback);
    }

    @Override
    public void confirmDelete(List<Long> ids, Runnable onConfirm) {
        dialogDelete.showForConfirm(ids.size(), onConfirm);
    }

    @Override
    public void showNotificationForNew() {
        Notification.show(getTranslation("notification_users_created"), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForEdit(Long id) {
        Notification.show(getTranslation("notification_users_saved"), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForDelete(List<Long> ids) {
        Notification.show(makeNotification("notification_users_deleted", "notification_users_blocked_all", ids.size()), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showDialogForBlock(SimpleUser user, SerializableConsumer<SimpleUser> callback) {
        blockDialog.openForView(user, callback);
    }

    @Override
    public void confirmExpel(List<Long> ids, Runnable onConfirm) {
        dialogExpel.showForConfirm(ids.size(), onConfirm);
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
        Notification.show(makeNotification("notification_users_accepted", "notification_users_accepted_all", ids.size()), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForReject(List<Long> ids) {
        Notification.show(makeNotification("notification_users_rejected", "notification_users_rejected_all", ids.size()), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForBlock(Long id) {
        Notification.show("notification_users_blocked", 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showNotificationForExpel(List<Long> ids) {
        Notification.show(getTranslation("notification_users_expel"), 3000, Notification.Position.TOP_END);
    }

    @Override
    public void showDialogForTutoring(SimpleUser user) {
        tutoringDialog.openForView(user);
    }

    @Override
    public void showNotificationForTutoring(Long id) {
        Notification.show("Test", 3000, Notification.Position.TOP_END);
    }

    private String makeNotification(String single, String plural, int count) {
        if (count > 1) {
            return getTranslation(plural, count);
        }
        return getTranslation(single);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        currentTab = event.getLocation().getQueryParameters().getSingleParameter("tab").orElse("visitor");
        tabSheet.setSelectedIndex(rolesInOrder.indexOf(currentTab));
    }
}
